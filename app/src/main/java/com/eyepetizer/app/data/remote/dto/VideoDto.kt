package com.eyepetizer.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 视频数据传输对象
 */
data class VideoDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("playUrl")
    val playUrl: String,
    @SerializedName("cover")
    val cover: CoverDto,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("author")
    val author: AuthorDto,
    @SerializedName("consumption")
    val consumption: ConsumptionDto,
    @SerializedName("releaseTime")
    val releaseTime: Long
)

data class CoverDto(
    @SerializedName("feed")
    val feed: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("blurred")
    val blurred: String?
)

data class AuthorDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("follow")
    val follow: FollowDto?
)

data class FollowDto(
    @SerializedName("itemType")
    val itemType: String,
    @SerializedName("itemId")
    val itemId: String,
    @SerializedName("followed")
    val followed: Boolean
)

data class ConsumptionDto(
    @SerializedName("collectionCount")
    val collectionCount: Long,
    @SerializedName("shareCount")
    val shareCount: Long,
    @SerializedName("replyCount")
    val replyCount: Long
)