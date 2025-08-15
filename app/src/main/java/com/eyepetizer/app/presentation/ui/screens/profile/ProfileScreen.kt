package com.eyepetizer.app.presentation.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eyepetizer.app.R
import com.eyepetizer.app.domain.model.User
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.presentation.ui.components.CompactVideoCard

/**
 * 个人中心屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.nav_profile),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            actions = {
                IconButton(onClick = { /* 设置 */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "设置")
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
                    onRetry = { /* 重试 */ }
                )
            }
            uiState.user != null -> {
                ProfileContent(
                    user = uiState.user,
                    watchHistory = uiState.watchHistory,
                    likedVideos = uiState.likedVideos,
                    collectedVideos = uiState.collectedVideos,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onVideoClick = { video ->
                        navController.navigate("video_detail/${video.id}")
                    },
                    onLogout = viewModel::logout,
                    onClearHistory = viewModel::clearWatchHistory,
                    onRemoveFromLiked = viewModel::removeFromLiked,
                    onRemoveFromCollected = viewModel::removeFromCollected
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    watchHistory: List<Video>,
    likedVideos: List<Video>,
    collectedVideos: List<Video>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onVideoClick: (Video) -> Unit,
    onLogout: () -> Unit,
    onClearHistory: () -> Unit,
    onRemoveFromLiked: (String) -> Unit,
    onRemoveFromCollected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // 用户信息卡片
            UserInfoCard(
                user = user,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        item {
            // 统计信息
            UserStatsRow(
                user = user,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            // 功能菜单
            FunctionMenuGrid(
                onLogout = onLogout,
                onClearHistory = onClearHistory,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        item {
            // 标签页选择器
            TabSelector(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            // 内容区域
            when (selectedTab) {
                0 -> VideoListSection(
                    title = "观看历史",
                    videos = watchHistory,
                    onVideoClick = onVideoClick,
                    emptyMessage = "暂无观看历史"
                )
                1 -> VideoListSection(
                    title = "我的点赞",
                    videos = likedVideos,
                    onVideoClick = onVideoClick,
                    emptyMessage = "暂无点赞视频"
                )
                2 -> VideoListSection(
                    title = "我的收藏",
                    videos = collectedVideos,
                    onVideoClick = onVideoClick,
                    emptyMessage = "暂无收藏视频"
                )
            }
        }
    }
}

@Composable
private fun UserInfoCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = user.username,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 用户名和认证标识
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                if (user.isVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "已认证",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 用户等级
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Lv.${user.level} ${user.getLevelText()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            
            if (user.bio.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun UserStatsRow(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            label = "关注",
            value = user.getFormattedFollowingCount()
        )
        StatItem(
            label = "粉丝",
            value = user.getFormattedFollowersCount()
        )
        StatItem(
            label = "获赞",
            value = user.getFormattedLikesCount()
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FunctionMenuGrid(
    onLogout: () -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "功能菜单",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionMenuItem(
                    icon = Icons.Default.History,
                    label = "清空历史",
                    onClick = onClearHistory
                )
                FunctionMenuItem(
                    icon = Icons.Default.Download,
                    label = "离线缓存",
                    onClick = { /* 离线缓存 */ }
                )
                FunctionMenuItem(
                    icon = Icons.Default.Feedback,
                    label = "意见反馈",
                    onClick = { /* 意见反馈 */ }
                )
                FunctionMenuItem(
                    icon = Icons.Default.Logout,
                    label = "退出登录",
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun FunctionMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TabSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("观看历史", "我的点赞", "我的收藏")
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, title ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selectedTab == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
                
                if (selectedTab == index) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(2.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoListSection(
    title: String,
    videos: List<Video>,
    onVideoClick: (Video) -> Unit,
    emptyMessage: String
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(videos) { video ->
                    CompactVideoCard(
                        video = video,
                        onVideoClick = onVideoClick,
                        onLikeClick = { /* 处理点赞 */ },
                        modifier = Modifier.width(280.dp)
                    )
                }
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