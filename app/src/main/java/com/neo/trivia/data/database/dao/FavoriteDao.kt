package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for FavoriteEntity.
 * Provides methods for querying and manipulating favorite question data in the database.
 */
@Dao
interface FavoriteDao {
    /**
     * Inserts a favorite question into the database.
     * If a favorite with the same questionId exists, it will be replaced.
     * @param favorite FavoriteEntity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    /**
     * Deletes a favorite question from the database.
     * @param favorite FavoriteEntity to delete
     */
    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    /**
     * Retrieves all favorite records.
     * @return Flow of FavoriteEntity objects with reactive updates
     */
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    /**
     * Retrieves all questions that are marked as favorites.
     * Performs a JOIN between questions and favorites tables.
     * @return Flow of QuestionEntity objects for favorited questions
     */
    @Query("SELECT q.* FROM questions q INNER JOIN favorites f ON q.id = f.questionId")
    fun getFavoriteQuestions(): Flow<List<QuestionEntity>>

    /**
     * Checks if a question is favorited.
     * @param questionId The unique identifier of the question
     * @return True if favorited, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE questionId = :questionId)")
    suspend fun isFavorite(questionId: String): Boolean

    /**
     * Removes a favorite question from the database.
     * @param questionId The unique identifier of the question to remove
     */
    @Query("DELETE FROM favorites WHERE questionId = :questionId")
    suspend fun removeFavorite(questionId: String)
}
