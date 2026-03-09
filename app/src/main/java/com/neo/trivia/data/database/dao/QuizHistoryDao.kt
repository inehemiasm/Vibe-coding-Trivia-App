package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.QuizHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for QuizHistoryEntity.
 * Provides methods for querying and manipulating quiz history data in the database.
 */
@Dao
interface QuizHistoryDao {
    /**
     * Inserts a quiz history entry into the database.
     * If an entry with the same composite key exists, it will be replaced.
     * @param quizHistory QuizHistoryEntity object to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizHistory: QuizHistoryEntity)

    /**
     * Inserts a list of quiz history entries into the database.
     * If an entry with the same composite key exists, it will be replaced.
     * @param quizHistories List of QuizHistoryEntity objects to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quizHistories: List<QuizHistoryEntity>)

    /**
     * Retrieves all quiz history entries, ordered by date (newest first).
     * @return Flow of QuizHistoryEntity objects
     */
    @Query("SELECT * FROM quiz_history ORDER BY quizDate DESC")
    fun getAllQuizHistory(): Flow<List<QuizHistoryEntity>>

    /**
     * Retrieves quiz history entries for a specific category, ordered by date (newest first).
     * @param categoryName The name of the category
     * @return Flow of QuizHistoryEntity objects
     */
    @Query("SELECT * FROM quiz_history WHERE categoryName = :categoryName ORDER BY quizDate DESC")
    fun getQuizHistoryByCategory(categoryName: String): Flow<List<QuizHistoryEntity>>

    /**
     * Retrieves the most recent quiz history entry for a specific category.
     * @param categoryName The name of the category
     * @return QuizHistoryEntity if found, null otherwise
     */
    @Query("SELECT * FROM quiz_history WHERE categoryName = :categoryName ORDER BY quizDate DESC LIMIT 1")
    suspend fun getLatestQuizHistoryByCategory(categoryName: String): QuizHistoryEntity?

    /**
     * Retrieves quiz history entries within a date range.
     * @param startDate Start date (formatted as string)
     * @param endDate End date (formatted as string)
     * @return Flow of QuizHistoryEntity objects
     */
    @Query("SELECT * FROM quiz_history WHERE quizDate BETWEEN :startDate AND :endDate ORDER BY quizDate DESC")
    fun getQuizHistoryByDateRange(
        startDate: String,
        endDate: String,
    ): Flow<List<QuizHistoryEntity>>

    /**
     * Retrieves all quiz history entries ordered by category and date.
     * @return Flow of QuizHistoryEntity objects
     */
    @Query("SELECT * FROM quiz_history ORDER BY categoryName, quizDate DESC")
    fun getQuizHistoryOrderedByCategory(): Flow<List<QuizHistoryEntity>>

    /**
     * Deletes a specific quiz history entry.
     * @param quizHistory QuizHistoryEntity object to delete
     */
    @Query("DELETE FROM quiz_history WHERE categoryName = :categoryName AND quizDate = :quizDate")
    suspend fun deleteQuizHistory(
        categoryName: String,
        quizDate: String,
    )

    /**
     * Deletes all quiz history entries.
     */
    @Query("DELETE FROM quiz_history")
    suspend fun clearAll()
}
