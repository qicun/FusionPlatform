package com.eyepetizer.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用程序Application类
 * 使用Hilt进行依赖注入
 */
@HiltAndroidApp
class EyepetizerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}