package com.eyepetizer.app.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 观看历史数据库实体
 */
@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: String,
    val userId: String,
    val watchProgress: Long, // 观看进度（秒）
    val watchDuration: Long, // 观看时长（秒）
    val isCompleted: Boolean = false,
    val watchTime: Long = System.currentTimeMillis()
)