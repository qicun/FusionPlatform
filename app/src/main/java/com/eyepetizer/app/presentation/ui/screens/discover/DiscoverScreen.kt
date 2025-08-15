package com.eyepetizer.app.presentation.ui.screens.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.presentation.ui.components.CategoryCard
import com.eyepetizer.app.presentation.ui.components.VideoCard

/**
 * 发现页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.nav_discover),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            actions = {
                IconButton(onClick = { /* 搜索功能 */ }) {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                }
            }
        )

        // 搜索框
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.searchVideos(it)
            },
            label = { Text("搜索视频") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        // 分类标签
        if (uiState.categories.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.categories) { category ->
                    CategoryCard(
                        category = category,
                        isSelected = category.id == uiState.selectedCategoryId,
                        onCategoryClick = { viewModel.loadVideosByCategory(it.id) },
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }

        // 内容区域
        when {
            searchQuery.isNotEmpty() -> {
                SearchResultsContent(
                    searchState = searchState,
                    onVideoClick = { video ->
                        navController.navigate("video_detail/${video.id}")
                    }
                )
            }
            uiState.isLoadingVideos -> {
                LoadingContent()
            }
            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = { /* 重试逻辑 */ }
                )
            }
            else -> {
                VideoGridContent(
                    videos = uiState.featuredVideos,
                    onVideoClick = { video ->
                        navController.navigate("video_detail/${video.id}")
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchResultsContent(
    searchState: SearchUiState,
    onVideoClick: (Video) -> Unit
) {
    when {
        searchState.isLoading -> {
            LoadingContent()
        }
        searchState.error != null -> {
            ErrorContent(
                error = searchState.error,
                onRetry = { /* 重试搜索 */ }
            )
        }
        searchState.results.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "未找到相关视频",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchState.results) { video ->
                    VideoCard(
                        video = video,
                        onVideoClick = onVideoClick,
                        onLikeClick = { /* 处理点赞 */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoGridContent(
    videos: List<Video>,
    onVideoClick: (Video) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                onVideoClick = onVideoClick,
                onLikeClick = { /* 处理点赞 */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
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
                text = "加载中...",
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
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}