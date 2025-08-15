package com.eyepetizer.app.domain.model

/**
 * 分类数据模型
 */
data class Category(
    val id: String,
    val name: String,
    val description: String = "",
    val icon: String = "",
    val videoCount: Int = 0,
    val isSelected: Boolean = false
)