package com.neo.trivia.data.database.dao

import androidx.room.*
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT q.* FROM questions q INNER JOIN favorites f ON q.id = f.questionId")
    fun getFavoriteQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE questionId = :questionId)")
    suspend fun isFavorite(questionId: String): Boolean

    @Query("DELETE FROM favorites WHERE questionId = :questionId")
    suspend fun removeFavorite(questionId: String)
}