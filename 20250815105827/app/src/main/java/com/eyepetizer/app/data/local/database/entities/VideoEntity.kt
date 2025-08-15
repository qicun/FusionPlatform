package com.eyepetizer.app.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 视频数据库实体
 */
@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val playUrl: String,
    val coverUrl: String,
    val duration: Int, // 秒
    val category: String,
    val authorId: String,
    val authorName: String,
    val authorIcon: String,
    val playCount: Long,
    val likeCount: Long,
    val shareCount: Long,
    val commentCount: Long,
    val isLiked: Boolean = false,
    val isFavorite: Boolean = false,
    val createTime: Long,
    val updateTime: Long = System.currentTimeMillis()
)