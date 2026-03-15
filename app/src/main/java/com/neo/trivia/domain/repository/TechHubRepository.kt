package com.neo.trivia.domain.repository

import com.neo.trivia.domain.model.TechHubPostData
import kotlinx.coroutines.flow.Flow

interface TechHubRepository {
    fun getSavedPosts(): Flow<List<TechHubPostData>>
    suspend fun savePost(post: TechHubPostData)
    suspend fun removePost(post: TechHubPostData)
    suspend fun isPostSaved(postId: String): Boolean
    suspend fun fetchDiscoverPosts(source: DiscoverySource): List<TechHubPostData>
    suspend fun getPostById(postId: String): TechHubPostData?
}

enum class DiscoverySource(val url: String, val displayName: String) {
    ANDROID_OFFICIAL("https://android-developers.googleblog.com/feeds/posts/default?alt=rss", "Android Official"),
    ANDROID_NEWS("https://9to5google.com/guides/android/feed/", "Android News"),
    IOS_OFFICIAL("https://developer.apple.com/news/rss/news.rss", "iOS Official"),
    IOS_NEWS("https://9to5mac.com/feed/", "iOS News")
}
