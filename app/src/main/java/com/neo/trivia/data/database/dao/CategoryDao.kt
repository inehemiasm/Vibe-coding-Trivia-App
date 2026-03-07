package com.neo.trivia.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neo.trivia.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CategoryEntity.
 * Provides methods for querying and manipulating category data in the database.
 */
@Dao
interface CategoryDao {
    /**
     * Inserts a category into the database.
     * If a category with the same ID exists, it will be replaced.
     * @param category CategoryEntity object to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    /**
     * Inserts a list of categories into the database.
     * If a category with the same ID exists, it will be replaced.
     * @param categories List of CategoryEntity objects to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    /**
     * Retrieves all categories from the database.
     * @return Flow of CategoryEntity objects
     */
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    /**
     * Retrieves a category by its ID.
     * @param categoryId The unique identifier of the category
     * @return CategoryEntity if found, null otherwise
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    /**
     * Retrieves a category by its name.
     * @param categoryName The name of the category
     * @return CategoryEntity if found, null otherwise
     */
    @Query("SELECT * FROM categories WHERE name = :categoryName")
    suspend fun getCategoryByName(categoryName: String): CategoryEntity?
}