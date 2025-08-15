package com.eyepetizer.app.presentation.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.eyepetizer.app.presentation.ui.theme.VideoControlBg
import com.eyepetizer.app.presentation.ui.theme.VideoOverlay

/**
 * 视频播放器组件
 */
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false,
    showControls: Boolean = true,
    onPlayerReady: ((ExoPlayer) -> Unit)? = null
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(autoPlay) }
    var isMuted by remember { mutableStateOf(false) }
    var showControlsOverlay by remember { mutableStateOf(false) }
    
    // 创建ExoPlayer实例
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = autoPlay
            
            // 添加播放状态监听器
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }
            })
            
            onPlayerReady?.invoke(this)
        }
    }
    
    // 在组件销毁时释放播放器
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // ExoPlayer视图
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false // 使用自定义控制器
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 自定义控制层
        if (showControls) {
            VideoControlsOverlay(
                isPlaying = isPlaying,
                isMuted = isMuted,
                showControls = showControlsOverlay,
                onPlayPauseClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                },
                onMuteClick = {
                    isMuted = !isMuted
                    exoPlayer.volume = if (isMuted) 0f else 1f
                },
                onControlsVisibilityChange = { visible ->
                    showControlsOverlay = visible
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 视频控制层覆盖组件
 */
@Composable
private fun VideoControlsOverlay(
    isPlaying: Boolean,
    isMuted: Boolean,
    showControls: Boolean,
    onPlayPauseClick: () -> Unit,
    onMuteClick: () -> Unit,
    onControlsVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var controlsVisible by remember { mutableStateOf(showControls) }
    
    // 自动隐藏控制器
    LaunchedEffect(controlsVisible) {
        if (controlsVisible && isPlaying) {
            kotlinx.coroutines.delay(3000) // 3秒后自动隐藏
            controlsVisible = false
            onControlsVisibilityChange(false)
        }
    }
    
    Box(
        modifier = modifier
            .background(
                if (controlsVisible) VideoOverlay else Color.Transparent
            )
    ) {
        // 点击区域显示/隐藏控制器
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (!controlsVisible) {
                        Modifier.background(Color.Transparent)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            // 播放/暂停按钮
            if (controlsVisible || !isPlaying) {
                IconButton(
                    onClick = {
                        onPlayPauseClick()
                        controlsVisible = true
                        onControlsVisibilityChange(true)
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            VideoControlBg,
                            RoundedCornerShape(32.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        // 音量控制按钮
        if (controlsVisible) {
            IconButton(
                onClick = {
                    onMuteClick()
                    controlsVisible = true
                    onControlsVisibilityChange(true)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(
                        VideoControlBg,
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = if (isMuted) "取消静音" else "静音",
                    tint = Color.White
                )
            }
        }
        
        // 点击空白区域切换控制器显示状态
        if (!controlsVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .then(
                        Modifier.clickable {
                            controlsVisible = true
                            onControlsVisibilityChange(true)
                        }
                    )
            )
        }
    }
}

/**
 * 简化版视频播放器，用于列表中的预览
 */
@Composable
fun VideoPreviewPlayer(
    videoUrl: String,
    coverUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false
) {
    var isLoading by remember { mutableStateOf(true) }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // 视频播放器
        VideoPlayer(
            videoUrl = videoUrl,
            autoPlay = autoPlay,
            showControls = false,
            onPlayerReady = { 
                isLoading = false 
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 加载指示器
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}