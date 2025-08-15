package com.eyepetizer.app.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 认证拦截器
 * 为请求添加必要的认证头和公共参数
 */
class AuthInterceptor : Interceptor {
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        
        // 添加公共查询参数
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("udid", getDeviceId())
            .addQueryParameter("vc", "6040000")
            .addQueryParameter("vn", "6.4.0")
            .addQueryParameter("deviceModel", "Android")
            .addQueryParameter("system", "Android")
            .build()
        
        // 添加请求头
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("User-Agent", "Eyepetizer/6.4.0 (Android)")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        
        return chain.proceed(newRequest)
    }
    
    private fun getDeviceId(): String {
        // 生成或获取设备唯一标识
        return "android_device_${System.currentTimeMillis()}"
    }
}