package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for QuestionEntity.
 * Provides methods for querying and manipulating question data in the database.
 */
@Dao
interface QuestionDao {
    /**
     * Retrieves all cached questions, ordered by creation timestamp (newest first).
     * @return Flow of QuestionEntity objects with reactive updates
     */
    @Query("SELECT * FROM questions ORDER BY createdAt DESC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    /**
     * Searches for questions containing the specified query in the question text.
     * Limited to 20 results for performance.
     * @param query Search query string for text matching
     * @return Flow of matching QuestionEntity objects
     */
    @Query("SELECT * FROM questions WHERE question LIKE '%' || :query || '%' LIMIT 20")
    fun searchQuestions(query: String): Flow<List<QuestionEntity>>

    /**
     * Inserts a list of questions into the database.
     * If a question with the same ID exists, it will be replaced.
     * @param questions List of QuestionEntity objects to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    /**
     * Clears all cached questions from the database.
     * Use with caution as this removes all question data.
     */
    @Query("DELETE FROM questions")
    suspend fun clearAll()

    /**
     * Retrieves a question by its ID.
     * @param questionId The unique identifier of the question
     * @return QuestionEntity if found, null otherwise
     */
    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: String): QuestionEntity?
}
