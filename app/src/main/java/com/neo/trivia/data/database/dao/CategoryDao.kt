package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    /**
     * Retrieves categories that have at least one question in the questions table.
     */
    @Query("""
        SELECT * FROM categories 
        WHERE name IN (SELECT DISTINCT category FROM questions)
    """)
    fun getCategoriesWithQuestions(): Flow<List<CategoryEntity>>
}
