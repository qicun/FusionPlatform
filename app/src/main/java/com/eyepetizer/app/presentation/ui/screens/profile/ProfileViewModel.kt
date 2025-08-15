package com.eyepetizer.app.presentation.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // 这里可以注入相关的UseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // 模拟加载用户数据
                val mockUser = User(
                    id = "user_001",
                    username = "开眼用户",
                    avatar = "https://picsum.photos/200/200?random=1",
                    email = "user@eyepetizer.com",
                    followingCount = 128,
                    followersCount = 256,
                    likesCount = 1024,
                    bio = "热爱生活，热爱视频创作",
                    isVerified = true
                )
                
                val mockWatchHistory = generateMockWatchHistory()
                val mockLikedVideos = generateMockLikedVideos()
                val mockCollectedVideos = generateMockCollectedVideos()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = mockUser,
                    watchHistory = mockWatchHistory,
                    likedVideos = mockLikedVideos,
                    collectedVideos = mockCollectedVideos
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载用户信息失败"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // 实现登出逻辑
            _uiState.value = ProfileUiState()
        }
    }

    fun clearWatchHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(watchHistory = emptyList())
        }
    }

    fun removeFromLiked(videoId: String) {
        viewModelScope.launch {
            val updatedList = _uiState.value.likedVideos.filter { it.id != videoId }
            _uiState.value = _uiState.value.copy(likedVideos = updatedList)
        }
    }

    fun removeFromCollected(videoId: String) {
        viewModelScope.launch {
            val updatedList = _uiState.value.collectedVideos.filter { it.id != videoId }
            _uiState.value = _uiState.value.copy(collectedVideos = updatedList)
        }
    }

    private fun generateMockWatchHistory(): List<Video> {
        return listOf(
            Video(
                id = "history_1",
                title = "最新科技产品评测",
                description = "深度评测最新发布的科技产品",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=10",
                duration = 300,
                category = "科技",
                authorId = "author_1",
                authorName = "科技达人",
                authorIcon = "https://picsum.photos/100/100?random=10",
                playCount = 25000,
                likeCount = 2100,
                shareCount = 450,
                commentCount = 320,
                createTime = System.currentTimeMillis() - 86400000 // 1天前
            ),
            Video(
                id = "history_2",
                title = "美食制作教程",
                description = "教你制作美味的家常菜",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=11",
                duration = 480,
                category = "美食",
                authorId = "author_2",
                authorName = "美食家",
                authorIcon = "https://picsum.photos/100/100?random=11",
                playCount = 18000,
                likeCount = 1500,
                shareCount = 280,
                commentCount = 190,
                createTime = System.currentTimeMillis() - 172800000 // 2天前
            )
        )
    }

    private fun generateMockLikedVideos(): List<Video> {
        return listOf(
            Video(
                id = "liked_1",
                title = "旅行Vlog - 探索未知城市",
                description = "跟随镜头探索这座美丽的城市",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=20",
                duration = 600,
                category = "旅行",
                authorId = "author_3",
                authorName = "旅行博主",
                authorIcon = "https://picsum.photos/100/100?random=20",
                playCount = 35000,
                likeCount = 3200,
                shareCount = 680,
                commentCount = 450,
                createTime = System.currentTimeMillis() - 259200000, // 3天前
                isLiked = true
            )
        )
    }

    private fun generateMockCollectedVideos(): List<Video> {
        return listOf(
            Video(
                id = "collected_1",
                title = "音乐MV精选",
                description = "最新流行音乐MV合集",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=30",
                duration = 240,
                category = "音乐",
                authorId = "author_4",
                authorName = "音乐制作人",
                authorIcon = "https://picsum.photos/100/100?random=30",
                playCount = 42000,
                likeCount = 3800,
                shareCount = 920,
                commentCount = 580,
                createTime = System.currentTimeMillis() - 345600000 // 4天前
            )
        )
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val watchHistory: List<Video> = emptyList(),
    val likedVideos: List<Video> = emptyList(),
    val collectedVideos: List<Video> = emptyList(),
    val error: String? = null
)