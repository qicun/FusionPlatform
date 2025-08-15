package com.eyepetizer.app.data.repository

import com.eyepetizer.app.data.local.database.dao.VideoDao
import com.eyepetizer.app.data.local.database.entities.VideoEntity
import com.eyepetizer.app.data.remote.api.EyepetizerApiService
import com.eyepetizer.app.data.remote.dto.VideoDto
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 视频仓库实现类
 */
@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val apiService: EyepetizerApiService,
    private val videoDao: VideoDao
) : VideoRepository {
    
    override fun getFeedVideos(refresh: Boolean): Flow<Result<List<Video>>> = flow {
        emit(Result.loading())
        
        try {
            if (!refresh) {
                // 先从本地数据库获取缓存数据
                val cachedVideos = videoDao.getAllVideos()
                cachedVideos.collect { entities ->
                    if (entities.isNotEmpty()) {
                        emit(Result.success(entities.map { it.toDomainModel() }))
                    }
                }
            }
            
            // 从网络获取最新数据
            val response = apiService.getFeed()
            if (response.isSuccessful) {
                response.body()?.let { feedResponse ->
                    val videos = feedResponse.itemList
                        .mapNotNull { it.data }
                        .map { it.toEntity() }
                    
                    // 保存到本地数据库
                    videoDao.insertVideos(videos)
                    
                    // 返回转换后的领域模型
                    emit(Result.success(videos.map { it.toDomainModel() }))
                }
            } else {
                emit(Result.error(Exception("网络请求失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.error(e))
        }
    }
    
    override fun getVideosByCategory(categoryId: String): Flow<Result<List<Video>>> = flow {
        emit(Result.loading())
        
        try {
            // 先从本地获取
            val cachedVideos = videoDao.getVideosByCategory(categoryId)
            cachedVideos.collect { entities ->
                if (entities.isNotEmpty()) {
                    emit(Result.success(entities.map { it.toDomainModel() }))
                }
            }
            
            // 从网络获取
            val response = apiService.getVideosByCategory(categoryId)
            if (response.isSuccessful) {
                response.body()?.let { feedResponse ->
                    val videos = feedResponse.itemList
                        .mapNotNull { it.data }
                        .map { it.toEntity() }
                    
                    videoDao.insertVideos(videos)
                    emit(Result.success(videos.map { it.toDomainModel() }))
                }
            }
        } catch (e: Exception) {
            emit(Result.error(e))
        }
    }
    
    override suspend fun getVideoDetail(videoId: String): Result<Video> {
        return try {
            // 先从本地获取
            val cachedVideo = videoDao.getVideoById(videoId)
            if (cachedVideo != null) {
                Result.success(cachedVideo.toDomainModel())
            } else {
                // 从网络获取
                val response = apiService.getVideoDetail(videoId)
                if (response.isSuccessful) {
                    response.body()?.let { videoDto ->
                        val videoEntity = videoDto.toEntity()
                        videoDao.insertVideo(videoEntity)
                        Result.success(videoEntity.toDomainModel())
                    } ?: Result.error(Exception("视频详情为空"))
                } else {
                    Result.error(Exception("获取视频详情失败"))
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun searchVideos(query: String): Result<List<Video>> {
        return try {
            val response = apiService.searchVideos(query)
            if (response.isSuccessful) {
                response.body()?.let { searchResponse ->
                    val videos = searchResponse.itemList.map { it.toEntity() }
                    Result.success(videos.map { it.toDomainModel() })
                } ?: Result.error(Exception("搜索结果为空"))
            } else {
                Result.error(Exception("搜索失败"))
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun likeVideo(videoId: String, isLiked: Boolean): Result<Unit> {
        return try {
            videoDao.updateLikeStatus(videoId, isLiked)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun favoriteVideo(videoId: String, isFavorite: Boolean): Result<Unit> {
        return try {
            videoDao.updateFavoriteStatus(videoId, isFavorite)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override fun getLikedVideos(): Flow<List<Video>> {
        return videoDao.getLikedVideos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getFavoriteVideos(): Flow<List<Video>> {
        return videoDao.getFavoriteVideos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
}

// 扩展函数：DTO转Entity
private fun VideoDto.toEntity(): VideoEntity {
    return VideoEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        playUrl = this.playUrl,
        coverUrl = this.cover.feed,
        duration = this.duration,
        category = this.category,
        authorId = this.author.id,
        authorName = this.author.name,
        authorIcon = this.author.icon,
        playCount = 0, // API中没有播放次数，设为0
        likeCount = this.consumption.collectionCount,
        shareCount = this.consumption.shareCount,
        commentCount = this.consumption.replyCount,
        createTime = this.releaseTime
    )
}

// 扩展函数：Entity转Domain Model
private fun VideoEntity.toDomainModel(): Video {
    return Video(
        id = this.id,
        title = this.title,
        description = this.description,
        playUrl = this.playUrl,
        coverUrl = this.coverUrl,
        duration = this.duration,
        category = this.category,
        authorId = this.authorId,
        authorName = this.authorName,
        authorIcon = this.authorIcon,
        playCount = this.playCount,
        likeCount = this.likeCount,
        shareCount = this.shareCount,
        commentCount = this.commentCount,
        isLiked = this.isLiked,
        isFavorite = this.isFavorite,
        createTime = this.createTime
    )
}