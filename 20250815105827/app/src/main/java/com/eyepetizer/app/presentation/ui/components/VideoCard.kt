package com.eyepetizer.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.presentation.ui.theme.LikeColor
import com.eyepetizer.app.presentation.ui.theme.ShareColor

/**
 * 视频卡片组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCard(
    video: Video,
    modifier: Modifier = Modifier,
    showPreview: Boolean = false,
    onVideoClick: (Video) -> Unit = {},
    onLikeClick: (Video) -> Unit = {},
    onShareClick: (Video) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onVideoClick(video) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 视频预览区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                if (showPreview) {
                    // 显示视频预览播放器
                    VideoPreviewPlayer(
                        videoUrl = video.playUrl,
                        coverUrl = video.coverUrl,
                        autoPlay = false,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // 显示封面图片
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(video.coverUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    )
                }
                
                // 时长标签
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.getFormattedDuration(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
            
            // 视频信息区域
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // 标题
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 描述
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 作者信息
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(video.authorIcon)
                            .crossfade(true)
                            .build(),
                        contentDescription = video.authorName,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = video.authorName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // 播放次数
                    Text(
                        text = "${video.getFormattedPlayCount()} 播放",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 操作按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 分类标签
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = video.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    // 操作按钮
                    Row {
                        // 点赞按钮
                        IconButton(
                            onClick = { onLikeClick(video) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "点赞",
                                    tint = if (video.isLiked) LikeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = video.getFormattedLikeCount(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (video.isLiked) LikeColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // 分享按钮
                        IconButton(
                            onClick = { onShareClick(video) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "分享",
                                tint = ShareColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 紧凑型视频卡片，用于列表显示
 */
@Composable
fun CompactVideoCard(
    video: Video,
    modifier: Modifier = Modifier,
    onVideoClick: (Video) -> Unit = {},
    onLikeClick: (Video) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onVideoClick(video) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // 封面图片
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 视频信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = video.authorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row {
                    Text(
                        text = video.getFormattedDuration(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${video.getFormattedPlayCount()} 播放",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 点赞按钮
            IconButton(
                onClick = { onLikeClick(video) }
            ) {
                Icon(
                    imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    tint = if (video.isLiked) LikeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}