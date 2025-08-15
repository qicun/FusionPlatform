package com.eyepetizer.app.domain.repository

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * 视频仓库接口
 */
interface VideoRepository {
    
    /**
     * 获取推荐视频流
     */
    fun getFeedVideos(refresh: Boolean = false): Flow<Result<List<Video>>>
    
    /**
     * 根据分类获取视频列表
     */
    fun getVideosByCategory(categoryId: String): Flow<Result<List<Video>>>
    
    /**
     * 获取视频详情
     */
    suspend fun getVideoDetail(videoId: String): Result<Video>
    
    /**
     * 搜索视频
     */
    suspend fun searchVideos(query: String): Result<List<Video>>
    
    /**
     * 点赞/取消点赞视频
     */
    suspend fun likeVideo(videoId: String, isLiked: Boolean): Result<Unit>
    
    /**
     * 收藏/取消收藏视频
     */
    suspend fun favoriteVideo(videoId: String, isFavorite: Boolean): Result<Unit>
    
    /**
     * 获取已点赞的视频列表
     */
    fun getLikedVideos(): Flow<List<Video>>
    
    /**
     * 获取收藏的视频列表
     */
    fun getFavoriteVideos(): Flow<List<Video>>
}