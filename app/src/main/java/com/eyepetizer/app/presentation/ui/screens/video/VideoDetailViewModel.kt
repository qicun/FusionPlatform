package com.eyepetizer.app.presentation.ui.screens.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.usecase.video.GetVideoDetailUseCase
import com.eyepetizer.app.domain.usecase.video.LikeVideoUseCase
import com.eyepetizer.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 视频详情页ViewModel
 */
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    private val getVideoDetailUseCase: GetVideoDetailUseCase,
    private val likeVideoUseCase: LikeVideoUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VideoDetailUiState())
    val uiState: StateFlow<VideoDetailUiState> = _uiState.asStateFlow()
    
    /**
     * 加载视频详情
     */
    fun loadVideoDetail(videoId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            when (val result = getVideoDetailUseCase(videoId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        video = result.data,
                        error = null
                    )
                    // 加载相关视频推荐
                    loadRelatedVideos(result.data.category)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "加载视频详情失败"
                    )
                }
                else -> {
                    // Loading状态已在上面处理
                }
            }
        }
    }
    
    /**
     * 加载相关视频推荐
     */
    private fun loadRelatedVideos(category: String) {
        viewModelScope.launch {
            // 这里可以调用获取相关视频的用例
            // 暂时使用模拟数据
            val mockRelatedVideos = generateMockRelatedVideos(category)
            _uiState.value = _uiState.value.copy(relatedVideos = mockRelatedVideos)
        }
    }
    
    /**
     * 点赞/取消点赞视频
     */
    fun likeVideo(videoId: String, isLiked: Boolean) {
        viewModelScope.launch {
            val result = likeVideoUseCase(videoId, isLiked)
            when (result) {
                is Result.Success -> {
                    // 更新当前视频的点赞状态
                    _uiState.value.video?.let { currentVideo ->
                        if (currentVideo.id == videoId) {
                            _uiState.value = _uiState.value.copy(
                                video = currentVideo.copy(isLiked = isLiked)
                            )
                        }
                    }
                    
                    // 更新相关视频列表中的点赞状态
                    val updatedRelatedVideos = _uiState.value.relatedVideos.map { video ->
                        if (video.id == videoId) {
                            video.copy(isLiked = isLiked)
                        } else {
                            video
                        }
                    }
                    _uiState.value = _uiState.value.copy(relatedVideos = updatedRelatedVideos)
                }
                is Result.Error -> {
                    // 处理点赞失败的情况
                    // 可以显示Toast或其他错误提示
                }
                else -> {
                    // Loading状态通常不需要处理
                }
            }
        }
    }
    
    /**
     * 生成模拟相关视频数据
     */
    private fun generateMockRelatedVideos(category: String): List<Video> {
        return listOf(
            Video(
                id = "related_1",
                title = "相关视频 1 - $category",
                description = "这是一个相关的视频描述",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=1",
                duration = 120,
                category = category,
                authorId = "author_1",
                authorName = "创作者1",
                authorIcon = "https://picsum.photos/100/100?random=1",
                playCount = 15000,
                likeCount = 1200,
                shareCount = 300,
                commentCount = 150,
                createTime = System.currentTimeMillis()
            ),
            Video(
                id = "related_2",
                title = "相关视频 2 - $category",
                description = "另一个相关的视频描述",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=2",
                duration = 180,
                category = category,
                authorId = "author_2",
                authorName = "创作者2",
                authorIcon = "https://picsum.photos/100/100?random=2",
                playCount = 8500,
                likeCount = 650,
                shareCount = 120,
                commentCount = 80,
                createTime = System.currentTimeMillis()
            )
        )
    }
}

/**
 * 视频详情页UI状态
 */
data class VideoDetailUiState(
    val isLoading: Boolean = false,
    val video: Video? = null,
    val relatedVideos: List<Video> = emptyList(),
    val error: String? = null
)