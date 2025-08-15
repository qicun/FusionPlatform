package com.eyepetizer.app.data.local.database.dao

import androidx.room.*
import com.eyepetizer.app.data.local.database.entities.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 观看历史数据访问对象
 */
@Dao
interface WatchHistoryDao {
    
    @Query("SELECT * FROM watch_history WHERE userId = :userId ORDER BY watchTime DESC")
    fun getWatchHistoryByUser(userId: String): Flow<List<WatchHistoryEntity>>
    
    @Query("SELECT * FROM watch_history WHERE videoId = :videoId AND userId = :userId")
    suspend fun getWatchHistory(videoId: String, userId: String): WatchHistoryEntity?
    
    @Query("SELECT * FROM watch_history WHERE userId = :userId ORDER BY watchTime DESC LIMIT :limit")
    fun getRecentWatchHistory(userId: String, limit: Int = 20): Flow<List<WatchHistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(watchHistory: WatchHistoryEntity)
    
    @Update
    suspend fun updateWatchHistory(watchHistory: WatchHistoryEntity)
    
    @Query("UPDATE watch_history SET watchProgress = :progress, watchDuration = :duration, isCompleted = :isCompleted WHERE videoId = :videoId AND userId = :userId")
    suspend fun updateWatchProgress(videoId: String, userId: String, progress: Long, duration: Long, isCompleted: Boolean)
    
    @Delete
    suspend fun deleteWatchHistory(watchHistory: WatchHistoryEntity)
    
    @Query("DELETE FROM watch_history WHERE userId = :userId")
    suspend fun deleteUserWatchHistory(userId: String)
    
    @Query("DELETE FROM watch_history WHERE watchTime < :timestamp")
    suspend fun deleteOldWatchHistory(timestamp: Long)
    
    @Query("DELETE FROM watch_history")
    suspend fun deleteAllWatchHistory()
}