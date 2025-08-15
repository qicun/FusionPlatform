package com.eyepetizer.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 首页推荐数据响应
 */
data class FeedResponse(
    @SerializedName("itemList")
    val itemList: List<FeedItem>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String?
)

data class FeedItem(
    @SerializedName("type")
    val type: String,
    @SerializedName("data")
    val data: VideoDto?,
    @SerializedName("tag")
    val tag: String?,
    @SerializedName("id")
    val id: Long,
    @SerializedName("adIndex")
    val adIndex: Int
)

data class SearchResponse(
    @SerializedName("itemList")
    val itemList: List<VideoDto>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String?
)