package com.eyepetizer.app.presentation.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 视频播放器ViewModel
 * 管理播放状态、进度、质量等
 */
class VideoPlayerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _bufferedPosition = MutableStateFlow(0L)
    val bufferedPosition: StateFlow<Long> = _bufferedPosition.asStateFlow()

    fun updatePlaybackState(isPlaying: Boolean) {
        _uiState.update { it.copy(isPlaying = isPlaying) }
    }

    fun updateLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateFullScreenState(isFullScreen: Boolean) {
        _uiState.update { it.copy(isFullScreen = isFullScreen) }
    }

    fun updateMuteState(isMuted: Boolean) {
        _uiState.update { it.copy(isMuted = isMuted) }
    }

    fun updateControlsVisibility(visible: Boolean) {
        _uiState.update { it.copy(showControls = visible) }
    }

    fun updatePlaybackSpeed(speed: Float) {
        _uiState.update { it.copy(playbackSpeed = speed) }
    }

    fun updateVideoQuality(quality: VideoQuality) {
        _uiState.update { it.copy(selectedQuality = quality) }
    }

    fun updatePosition(position: Long) {
        _playbackPosition.value = position
    }

    fun updateDuration(duration: Long) {
        _duration.value = duration
    }

    fun updateBufferedPosition(position: Long) {
        _bufferedPosition.value = position
    }

    fun savePlaybackPosition(videoId: String, position: Long) {
        viewModelScope.launch {
            // TODO: 保存播放位置到数据库
            // 实现视频书签功能
        }
    }

    fun getPlaybackPosition(videoId: String): Long {
        // TODO: 从数据库获取保存的播放位置
        return 0L
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun toggleMute() {
        _uiState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleFullScreen() {
        _uiState.update { it.copy(isFullScreen = !it.isFullScreen) }
    }

    fun seekTo(position: Long) {
        _playbackPosition.value = position
    }

    fun adjustVolume(delta: Float) {
        val currentVolume = _uiState.value.volume
        val newVolume = (currentVolume + delta).coerceIn(0f, 1f)
        _uiState.update { it.copy(volume = newVolume) }
    }

    fun adjustBrightness(delta: Float) {
        val currentBrightness = _uiState.value.brightness
        val newBrightness = (currentBrightness + delta).coerceIn(0f, 1f)
        _uiState.update { it.copy(brightness = newBrightness) }
    }
}

/**
 * 视频播放器UI状态
 */
data class VideoPlayerUiState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val isFullScreen: Boolean = false,
    val isMuted: Boolean = false,
    val showControls: Boolean = true,
    val playbackSpeed: Float = 1.0f,
    val selectedQuality: VideoQuality = VideoQuality.AUTO,
    val volume: Float = 1.0f,
    val brightness: Float = 0.5f,
    val error: String? = null
)

/**
 * 视频质量枚举
 */
enum class VideoQuality(val displayName: String, val value: String) {
    AUTO("自动", "auto"),
    HD_1080P("1080P", "1080p"),
    HD_720P("720P", "720p"),
    SD_480P("480P", "480p"),
    SD_360P("360P", "360p")
}

/**
 * 播放速度选项
 */
enum class PlaybackSpeed(val displayName: String, val value: Float) {
    SPEED_0_5X("0.5x", 0.5f),
    SPEED_0_75X("0.75x", 0.75f),
    SPEED_1X("1.0x", 1.0f),
    SPEED_1_25X("1.25x", 1.25f),
    SPEED_1_5X("1.5x", 1.5f),
    SPEED_2X("2.0x", 2.0f)
}