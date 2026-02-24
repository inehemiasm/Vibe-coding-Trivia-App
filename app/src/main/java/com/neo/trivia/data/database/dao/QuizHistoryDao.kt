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
 * Stores statistics from completed quizzes.
 */
@Dao
interface QuizHistoryDao {
    /**
     * Inserts a quiz history record into the database.
     * @param history QuizHistoryEntity to insert
     * @return The row ID of the inserted record, or -1 on failure
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: QuizHistoryEntity): Long

    /**
     * Retrieves all quiz history records, ordered by timestamp (newest first).
     * Limited to the most recent 50 quizzes.
     * @return Flow of QuizHistoryEntity objects with reactive updates
     */
    @Query("SELECT * FROM quiz_history ORDER BY timestamp DESC LIMIT 50")
    fun getAllHistory(): Flow<List<QuizHistoryEntity>>

    /**
     * Deletes all quiz history records.
     * Use with caution as this removes all history data.
     */
    @Query("DELETE FROM quiz_history")
    suspend fun deleteAll()

    /**
     * Retrieves the total count of quiz history records.
     * @return The number of quiz history records
     */
    @Query("SELECT COUNT(*) FROM quiz_history")
    suspend fun getHistoryCount(): Int
}