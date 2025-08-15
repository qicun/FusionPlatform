package com.eyepetizer.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.eyepetizer.app.data.local.database.dao.*
import com.eyepetizer.app.data.local.database.entities.*
import com.eyepetizer.app.utils.Constants

/**
 * 应用数据库配置
 */
@Database(
    entities = [
        VideoEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        WatchHistoryEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun videoDao(): VideoDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}