package com.eyepetizer.app.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eyepetizer.app.R
import com.eyepetizer.app.presentation.ui.components.VideoCard
import com.eyepetizer.app.presentation.navigation.Screen

/**
 * 首页屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.home_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        )
        
        // 内容区域
        when {
            uiState.isLoading -> {
                LoadingContent()
            }
            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = { viewModel.loadFeedVideos(refresh = true) }
                )
            }
            else -> {
                VideoFeedContent(
                    videos = uiState.videos,
                    onVideoClick = { video ->
                        // 导航到视频详情页
                        // navController.navigate(Screen.VideoDetail.createRoute(video.id))
                    },
                    onLikeClick = { video ->
                        viewModel.likeVideo(video.id, !video.isLiked)
                    },
                    onRefresh = {
                        viewModel.loadFeedVideos(refresh = true)
                    }
                )
            }
        }
    }
    
    // 初始加载数据
    LaunchedEffect(Unit) {
        viewModel.loadFeedVideos()
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyMedium
            )
        }
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
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun VideoFeedContent(
    videos: List<com.eyepetizer.app.domain.model.Video>,
    onVideoClick: (com.eyepetizer.app.domain.model.Video) -> Unit,
    onLikeClick: (com.eyepetizer.app.domain.model.Video) -> Unit,
    onRefresh: () -> Unit
) {
    if (videos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.empty_data),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onLikeClick = { onLikeClick(video) }
                )
            }
        }
    }
}