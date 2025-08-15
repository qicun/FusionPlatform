package com.eyepetizer.app.data.local.database.dao

import androidx.room.*
import com.eyepetizer.app.data.local.database.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

/**
 * 视频数据访问对象
 */
@Dao
interface VideoDao {
    
    @Query("SELECT * FROM videos ORDER BY updateTime DESC")
    fun getAllVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE category = :category ORDER BY updateTime DESC")
    fun getVideosByCategory(category: String): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): VideoEntity?
    
    @Query("SELECT * FROM videos WHERE isLiked = 1 ORDER BY updateTime DESC")
    fun getLikedVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE isFavorite = 1 ORDER BY updateTime DESC")
    fun getFavoriteVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchVideos(query: String): List<VideoEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)
    
    @Update
    suspend fun updateVideo(video: VideoEntity)
    
    @Query("UPDATE videos SET isLiked = :isLiked WHERE id = :videoId")
    suspend fun updateLikeStatus(videoId: String, isLiked: Boolean)
    
    @Query("UPDATE videos SET isFavorite = :isFavorite WHERE id = :videoId")
    suspend fun updateFavoriteStatus(videoId: String, isFavorite: Boolean)
    
    @Delete
    suspend fun deleteVideo(video: VideoEntity)
    
    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()
    
    @Query("DELETE FROM videos WHERE updateTime < :timestamp")
    suspend fun deleteOldVideos(timestamp: Long)
}