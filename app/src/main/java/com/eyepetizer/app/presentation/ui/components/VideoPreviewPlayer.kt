package com.eyepetizer.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * 视频预览播放器组件
 * 用于在卡片中显示视频预览
 */
@Composable
fun VideoPreviewPlayer(
    videoUrl: String,
    coverUrl: String,
    autoPlay: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showPlayer by remember { mutableStateOf(autoPlay) }
    
    Box(modifier = modifier) {
        if (showPlayer) {
            // 显示实际的视频播放器
            VideoPlayer(
                videoUrl = videoUrl,
                autoPlay = true,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 显示封面图片和播放按钮
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "视频封面",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // 播放按钮覆盖层
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { showPlayer = true },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "播放",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}