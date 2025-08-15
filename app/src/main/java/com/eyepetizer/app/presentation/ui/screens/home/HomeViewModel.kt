package com.eyepetizer.app.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.usecase.video.GetFeedVideosUseCase
import com.eyepetizer.app.domain.usecase.video.LikeVideoUseCase
import com.eyepetizer.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeedVideosUseCase: GetFeedVideosUseCase,
    private val likeVideoUseCase: LikeVideoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadFeedVideos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // 模拟加载推荐视频数据
                val mockVideos = generateMockFeedVideos()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    videos = mockVideos
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载视频失败"
                )
            }
        }
    }

    fun likeVideo(videoId: String) {
        viewModelScope.launch {
            try {
                // 这里应该调用实际的UseCase
                // likeVideoUseCase(videoId)
                
                // 模拟点赞操作
                val updatedVideos = _uiState.value.videos.map { video ->
                    if (video.id == videoId) {
                        video.copy(
                            isLiked = !video.isLiked,
                            likeCount = if (video.isLiked) video.likeCount - 1 else video.likeCount + 1
                        )
                    } else {
                        video
                    }
                }
                
                _uiState.value = _uiState.value.copy(videos = updatedVideos)
            } catch (e: Exception) {
                // 处理点赞失败
            }
        }
    }

    private fun generateMockFeedVideos(): List<Video> {
        return listOf(
            Video(
                id = "feed_1",
                title = "开眼精选：2024年度最佳创意视频合集",
                description = "汇聚全年最具创意和视觉冲击力的视频作品，带你领略不一样的视觉盛宴",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=feed1",
                duration = 480,
                category = "精选",
                authorId = "eyepetizer_official",
                authorName = "开眼精选",
                authorIcon = "https://picsum.photos/100/100?random=official",
                playCount = 1250000,
                likeCount = 85000,
                shareCount = 12000,
                commentCount = 6500,
                createTime = System.currentTimeMillis() - 3600000, // 1小时前
                isLiked = false
            ),
            Video(
                id = "feed_2",
                title = "科技前沿：AI如何改变我们的生活",
                description = "深度探讨人工智能技术在日常生活中的应用和未来发展趋势",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=feed2",
                duration = 720,
                category = "科技",
                authorId = "tech_explorer",
                authorName = "科技探索者",
                authorIcon = "https://picsum.photos/100/100?random=tech",
                playCount = 890000,
                likeCount = 62000,
                shareCount = 8900,
                commentCount = 4200,
                createTime = System.currentTimeMillis() - 7200000, // 2小时前
                isLiked = false
            ),
            Video(
                id = "feed_3",
                title = "美食之旅：探寻世界各地的特色料理",
                description = "跟随镜头走遍世界，品尝各国特色美食，感受不同文化的味蕾碰撞",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=feed3",
                duration = 600,
                category = "美食",
                authorId = "food_traveler",
                authorName = "美食旅行家",
                authorIcon = "https://picsum.photos/100/100?random=food",
                playCount = 650000,
                likeCount = 45000,
                shareCount = 6800,
                commentCount = 3200,
                createTime = System.currentTimeMillis() - 10800000, // 3小时前
                isLiked = true
            ),
            Video(
                id = "feed_4",
                title = "自然奇观：地球上最美的风景",
                description = "用镜头记录地球上最壮观的自然景色，感受大自然的鬼斧神工",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=feed4",
                duration = 540,
                category = "旅行",
                authorId = "nature_photographer",
                authorName = "自然摄影师",
                authorIcon = "https://picsum.photos/100/100?random=nature",
                playCount = 780000,
                likeCount = 58000,
                shareCount = 9200,
                commentCount = 3800,
                createTime = System.currentTimeMillis() - 14400000, // 4小时前
                isLiked = false
            ),
            Video(
                id = "feed_5",
                title = "音乐无界：跨越文化的旋律",
                description = "探索不同文化背景下的音乐艺术，感受音乐跨越国界的魅力",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=feed5",
                duration = 420,
                category = "音乐",
                authorId = "music_curator",
                authorName = "音乐策展人",
                authorIcon = "https://picsum.photos/100/100?random=music",
                playCount = 520000,
                likeCount = 38000,
                shareCount = 5600,
                commentCount = 2800,
                createTime = System.currentTimeMillis() - 18000000, // 5小时前
                isLiked = false
            )
        )
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val videos: List<Video> = emptyList(),
    val error: String? = null
)