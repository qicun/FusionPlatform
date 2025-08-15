package com.eyepetizer.app.data.local.database.dao

import androidx.room.*
import com.eyepetizer.app.data.local.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 分类数据访问对象
 */
@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?
    
    @Query("SELECT * FROM categories WHERE isSelected = 1")
    fun getSelectedCategories(): Flow<List<CategoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Query("UPDATE categories SET isSelected = :isSelected WHERE id = :categoryId")
    suspend fun updateCategorySelection(categoryId: String, isSelected: Boolean)
    
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}