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

/**
 * 首页ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeedVideosUseCase: GetFeedVideosUseCase,
    private val likeVideoUseCase: LikeVideoUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    /**
     * 加载推荐视频流
     */
    fun loadFeedVideos(refresh: Boolean = false) {
        viewModelScope.launch {
            getFeedVideosUseCase(refresh).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            videos = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "未知错误"
                        )
                    }
                }
            }
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
                    // 更新本地状态
                    val updatedVideos = _uiState.value.videos.map { video ->
                        if (video.id == videoId) {
                            video.copy(isLiked = isLiked)
                        } else {
                            video
                        }
                    }
                    _uiState.value = _uiState.value.copy(videos = updatedVideos)
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
}

/**
 * 首页UI状态
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val videos: List<Video> = emptyList(),
    val error: String? = null
)