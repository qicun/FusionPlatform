package com.eyepetizer.app.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户数据库实体
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val nickname: String,
    val avatar: String,
    val email: String? = null,
    val phone: String? = null,
    val bio: String? = null,
    val followingCount: Int = 0,
    val followerCount: Int = 0,
    val videoCount: Int = 0,
    val likeCount: Int = 0,
    val isFollowed: Boolean = false,
    val createTime: Long,
    val updateTime: Long = System.currentTimeMillis()
)