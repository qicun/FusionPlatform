package com.eyepetizer.app.presentation.ui.screens.video

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.presentation.ui.components.CompactVideoCard
import com.eyepetizer.app.presentation.ui.components.VideoPlayer

/**
 * 视频详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    videoId: String,
    navController: NavController,
    viewModel: VideoDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(videoId) {
        viewModel.loadVideoDetail(videoId)
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = { Text("视频详情") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            }
        )
        
        when {
            uiState.isLoading -> {
                LoadingContent()
            }
            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = { viewModel.loadVideoDetail(videoId) }
                )
            }
            uiState.video != null -> {
                VideoDetailContent(
                    video = uiState.video,
                    relatedVideos = uiState.relatedVideos,
                    onLikeClick = { video ->
                        viewModel.likeVideo(video.id, !video.isLiked)
                    },
                    onRelatedVideoClick = { video ->
                        // 导航到新的视频详情页
                        navController.navigate("video_detail/${video.id}")
                    }
                )
            }
        }
    }
}

@Composable
private fun VideoDetailContent(
    video: Video,
    relatedVideos: List<Video>,
    onLikeClick: (Video) -> Unit,
    onRelatedVideoClick: (Video) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // 视频播放器
            VideoPlayer(
                videoUrl = video.playUrl,
                autoPlay = true,
                showControls = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        
        item {
            // 视频信息
            VideoInfoSection(
                video = video,
                onLikeClick = onLikeClick,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        item {
            // 作者信息
            AuthorInfoSection(
                video = video,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        item {
            // 相关视频标题
            Text(
                text = "相关推荐",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        item {
            // 相关视频横向列表
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(relatedVideos) { relatedVideo ->
                    CompactVideoCard(
                        video = relatedVideo,
                        onVideoClick = onRelatedVideoClick,
                        onLikeClick = onLikeClick,
                        modifier = Modifier.width(280.dp)
                    )
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun VideoInfoSection(
    video: Video,
    onLikeClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 标题
        Text(
            text = video.title,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 统计信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${video.getFormattedPlayCount()} 播放",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "${video.getFormattedLikeCount()} 点赞",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "${video.shareCount} 分享",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 描述
        Text(
            text = video.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 操作按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onLikeClick(video) },
                colors = if (video.isLiked) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) {
                Text(if (video.isLiked) "已点赞" else "点赞")
            }
            
            OutlinedButton(
                onClick = { /* 收藏功能 */ }
            ) {
                Text("收藏")
            }
            
            OutlinedButton(
                onClick = { /* 分享功能 */ }
            ) {
                Text("分享")
            }
        }
    }
}

@Composable
private fun AuthorInfoSection(
    video: Video,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 作者头像
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.authorIcon)
                    .crossfade(true)
                    .build(),
                contentDescription = video.authorName,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.authorName,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = video.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            OutlinedButton(
                onClick = { /* 关注功能 */ }
            ) {
                Text("关注")
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}