package com.eyepetizer.app.data.remote.api

import com.eyepetizer.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 开眼API服务接口
 */
interface EyepetizerApiService {
    
    /**
     * 获取首页推荐视频流
     */
    @GET("api/v2/feed")
    suspend fun getFeed(
        @Query("num") num: Int = 10,
        @Query("udid") udid: String = "",
        @Query("vc") vc: Int = 6040000,
        @Query("vn") vn: String = "6.4.0",
        @Query("deviceModel") deviceModel: String = "Android",
        @Query("first") first: Boolean = true,
        @Query("system") system: String = "Android"
    ): Response<FeedResponse>
    
    /**
     * 获取更多推荐视频
     */
    @GET
    suspend fun getMoreFeed(@Url url: String): Response<FeedResponse>
    
    /**
     * 获取分类列表
     */
    @GET("api/v4/categories")
    suspend fun getCategories(): Response<CategoryListResponse>
    
    /**
     * 根据分类获取视频列表
     */
    @GET("api/v4/categories/videoList")
    suspend fun getVideosByCategory(
        @Query("id") categoryId: String,
        @Query("udid") udid: String = "",
        @Query("vc") vc: Int = 6040000,
        @Query("vn") vn: String = "6.4.0"
    ): Response<FeedResponse>
    
    /**
     * 获取视频详情
     */
    @GET("api/v2/video/{id}")
    suspend fun getVideoDetail(
        @Path("id") videoId: String
    ): Response<VideoDto>
    
    /**
     * 搜索视频
     */
    @GET("api/v1/search")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("start") start: Int = 0,
        @Query("num") num: Int = 20
    ): Response<SearchResponse>
    
    /**
     * 获取热门搜索关键词
     */
    @GET("api/v3/queries/hot")
    suspend fun getHotSearchKeywords(): Response<List<String>>
    
    /**
     * 获取相关视频推荐
     */
    @GET("api/v4/video/related")
    suspend fun getRelatedVideos(
        @Query("id") videoId: String
    ): Response<FeedResponse>
}