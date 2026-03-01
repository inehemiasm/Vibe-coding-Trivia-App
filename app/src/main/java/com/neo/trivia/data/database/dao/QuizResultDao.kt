package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<QuizResultEntity>)

    @Delete
    suspend fun delete(result: QuizResultEntity)

    @Query("DELETE FROM quiz_results WHERE id IN (SELECT id FROM quiz_results ORDER BY timestamp ASC LIMIT :count - (SELECT COUNT(*) FROM quiz_results))")
    suspend fun deleteOldest(count: Int)

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC LIMIT 10")
    fun getRecentResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC LIMIT 1")
    fun getLatestResult(): Flow<QuizResultEntity?>

    @Query("SELECT * FROM quiz_results WHERE id = :id")
    suspend fun getResultById(id: String): QuizResultEntity?

    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}