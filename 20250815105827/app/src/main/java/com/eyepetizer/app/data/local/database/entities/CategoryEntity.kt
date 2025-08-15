package com.eyepetizer.app.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 分类数据库实体
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val coverUrl: String,
    val videoCount: Int,
    val isSelected: Boolean = false,
    val sortOrder: Int = 0
)