package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.MediumPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TechHubPostDao {
    @Query("SELECT * FROM medium_posts ORDER BY savedAt DESC")
    fun getAllSavedPosts(): Flow<List<MediumPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePost(post: MediumPostEntity)

    @Delete
    suspend fun deletePost(post: MediumPostEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM medium_posts WHERE id = :id)")
    suspend fun isPostSaved(id: String): Boolean

    @Query("SELECT * FROM medium_posts WHERE id = :id")
    suspend fun getPostById(id: String): MediumPostEntity?
}
