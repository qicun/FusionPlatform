package com.eyepetizer.app.domain.model

/**
 * 视频领域模型
 */
data class Video(
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
    val createTime: Long
) {
    /**
     * 格式化时长显示
     */
    fun getFormattedDuration(): String {
        val minutes = duration / 60
        val seconds = duration % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    
    /**
     * 格式化播放次数显示
     */
    fun getFormattedPlayCount(): String {
        return when {
            playCount >= 10000 -> "${playCount / 10000}万"
            playCount >= 1000 -> "${playCount / 1000}k"
            else -> playCount.toString()
        }
    }
    
    /**
     * 格式化点赞数显示
     */
    fun getFormattedLikeCount(): String {
        return when {
            likeCount >= 10000 -> "${likeCount / 10000}万"
            likeCount >= 1000 -> "${likeCount / 1000}k"
            else -> likeCount.toString()
        }
    }
}