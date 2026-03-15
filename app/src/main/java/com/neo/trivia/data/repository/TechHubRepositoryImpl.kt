package com.neo.trivia.data.repository

import android.util.Xml
import com.neo.trivia.data.api.MediumApi
import com.neo.trivia.data.database.dao.TechHubPostDao
import com.neo.trivia.data.database.entity.MediumPostEntity
import com.neo.trivia.domain.model.TechHubPostData
import com.neo.trivia.domain.repository.DiscoverySource
import com.neo.trivia.domain.repository.TechHubRepository
import com.neo.trivia.util.HtmlTextCleaner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechHubRepositoryImpl @Inject constructor(
    private val techHubPostDao: TechHubPostDao,
    private val mediumApi: MediumApi
) : TechHubRepository {

    private var discoverCache = mutableListOf<TechHubPostData>()

    override fun getSavedPosts(): Flow<List<TechHubPostData>> {
        return techHubPostDao.getAllSavedPosts().map { entities ->
            entities.map { it.toDomain(true) }
        }
    }

    override suspend fun savePost(post: TechHubPostData) {
        techHubPostDao.savePost(post.toEntity())
    }

    override suspend fun removePost(post: TechHubPostData) {
        techHubPostDao.deletePost(post.toEntity())
    }

    override suspend fun isPostSaved(postId: String): Boolean {
        return techHubPostDao.isPostSaved(postId)
    }

    override suspend fun fetchDiscoverPosts(source: DiscoverySource): List<TechHubPostData> = withContext(Dispatchers.IO) {
        try {
            val responseBody = mediumApi.fetchRssFeed(source.url)
            val xmlString = responseBody.string()
            val posts = parseRss(xmlString)
            
            val finalPosts = posts.map { it.copy(isSaved = isPostSaved(it.id)) }
            discoverCache.clear()
            discoverCache.addAll(finalPosts)
            finalPosts
        } catch (e: Exception) {
            Timber.e(e, "Error fetching RSS feed from ${source.displayName}")
            emptyList()
        }
    }

    override suspend fun getPostById(postId: String): TechHubPostData? {
        val savedEntity = techHubPostDao.getPostById(postId)
        if (savedEntity != null) return savedEntity.toDomain(true)
        return discoverCache.find { it.id == postId }
    }

    private fun parseRss(xmlData: String): List<TechHubPostData> {
        val posts = mutableListOf<TechHubPostData>()
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(xmlData.reader())

        var eventType = parser.eventType
        var currentPost = PostBuilder()
        var insideItem = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val name = parser.name
            val namespace = parser.namespace

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (name) {
                        "item" -> {
                            insideItem = true
                            currentPost = PostBuilder()
                        }
                        "title" -> if (insideItem) currentPost.title = HtmlTextCleaner.cleanHtmlText(parser.nextText())
                        "creator" -> if (insideItem) currentPost.author = parser.nextText()
                        "link" -> if (insideItem) {
                            val link = parser.nextText()
                            currentPost.url = link
                            currentPost.id = link.hashCode().toString()
                        }
                        "description" -> if (insideItem) {
                            val desc = parser.nextText()
                            if (currentPost.content.isEmpty()) {
                                currentPost.content = processContentSnippet(desc)
                            }
                            if (currentPost.imageUrl == null) {
                                currentPost.imageUrl = extractImageUrl(desc)
                            }
                        }
                        "encoded" -> if (insideItem && namespace.contains("content")) {
                            val content = parser.nextText()
                            currentPost.content = processContentSnippet(content)
                            if (currentPost.imageUrl == null) {
                                currentPost.imageUrl = extractImageUrl(content)
                            }
                        }
                        "thumbnail" -> if (insideItem && currentPost.imageUrl == null) {
                            currentPost.imageUrl = parser.getAttributeValue(null, "url")
                        }
                        "content" -> if (insideItem && namespace.contains("media") && currentPost.imageUrl == null) {
                             currentPost.imageUrl = parser.getAttributeValue(null, "url")
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (name == "item") {
                        posts.add(currentPost.build())
                        insideItem = false
                    }
                }
            }
            eventType = parser.next()
        }
        return posts
    }

    private fun processContentSnippet(html: String): String {
        val cleaned = HtmlTextCleaner.cleanHtmlText(html)
        // Remove common RSS teaser suffixes
        return cleaned
            .replace(Regex("\\s*\\[\\u2026\\]$"), "...") // Remove […]
            .replace(Regex("\\s*more\\.*$"), "...")       // Remove more...
            .replace(Regex("\\s*read more$"), "...")     // Remove read more
            .trim()
    }

    private fun extractImageUrl(html: String): String? {
        val match = Regex("<img[^>]+src=\"([^\"]+)\"").find(html)
        return match?.groupValues?.get(1)
    }

    private class PostBuilder {
        var id: String = ""
        var title: String = ""
        var author: String = "Unknown"
        var content: String = ""
        var imageUrl: String? = null
        var url: String = ""

        fun build() = TechHubPostData(id, title, author, content, imageUrl, url)
    }

    private fun TechHubPostData.toEntity() = MediumPostEntity(
        id = id,
        title = title,
        author = author,
        content = content,
        imageUrl = imageUrl,
        url = url
    )

    private fun MediumPostEntity.toDomain(isSaved: Boolean) = TechHubPostData(
        id = id,
        title = title,
        author = author,
        content = content,
        imageUrl = imageUrl,
        url = url,
        isSaved = isSaved
    )
}
