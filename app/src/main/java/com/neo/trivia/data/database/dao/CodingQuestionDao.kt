package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.neo.trivia.data.database.entity.CodingQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CodingQuestionDao {
    @Query("SELECT * FROM coding_questions ORDER BY createdAt DESC")
    fun getAllCodingQuestions(): Flow<List<CodingQuestionEntity>>

    @Query("SELECT * FROM coding_questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<CodingQuestionEntity>>

    @Query("SELECT DISTINCT category FROM coding_questions")
    fun getCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<CodingQuestionEntity>)

    @Update
    suspend fun updateQuestion(question: CodingQuestionEntity)

    @Query("SELECT * FROM coding_questions WHERE id = :id")
    suspend fun getQuestionById(id: String): CodingQuestionEntity?

    @Query("SELECT COUNT(*) FROM coding_questions")
    suspend fun getCount(): Int
}
