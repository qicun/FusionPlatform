package com.eyepetizer.app.domain.model

/**
 * 用户数据模型
 */
data class User(
    val id: String,
    val username: String,
    val avatar: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val likesCount: Int = 0,
    val videosCount: Int = 0,
    val isVerified: Boolean = false,
    val level: Int = 1,
    val createTime: Long = System.currentTimeMillis(),
    val lastLoginTime: Long = System.currentTimeMillis()
) {
    /**
     * 获取用户等级显示文本
     */
    fun getLevelText(): String {
        return when (level) {
            in 1..10 -> "新手"
            in 11..30 -> "达人"
            in 31..50 -> "专家"
            else -> "大师"
        }
    }

    /**
     * 获取格式化的粉丝数
     */
    fun getFormattedFollowersCount(): String {
        return when {
            followersCount >= 10000 -> "${followersCount / 10000}万"
            followersCount >= 1000 -> "${followersCount / 1000}k"
            else -> followersCount.toString()
        }
    }

    /**
     * 获取格式化的关注数
     */
    fun getFormattedFollowingCount(): String {
        return when {
            followingCount >= 10000 -> "${followingCount / 10000}万"
            followingCount >= 1000 -> "${followingCount / 1000}k"
            else -> followingCount.toString()
        }
    }

    /**
     * 获取格式化的点赞数
     */
    fun getFormattedLikesCount(): String {
        return when {
            likesCount >= 10000 -> "${likesCount / 10000}万"
            likesCount >= 1000 -> "${likesCount / 1000}k"
            else -> likesCount.toString()
        }
    }
}