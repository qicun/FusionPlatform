package com.eyepetizer.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 分类数据传输对象
 */
data class CategoryDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("bgPicture")
    val bgPicture: String,
    @SerializedName("bgColor")
    val bgColor: String?,
    @SerializedName("videoCount")
    val videoCount: Int
)

data class CategoryListResponse(
    @SerializedName("itemList")
    val itemList: List<CategoryDto>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String?
)