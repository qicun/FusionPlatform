package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import javax.inject.Inject

/**
 * 点赞视频用例
 */
class LikeVideoUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    
    /**
     * 执行点赞/取消点赞操作
     * @param videoId 视频ID
     * @param isLiked 是否点赞
     */
    suspend operator fun invoke(videoId: String, isLiked: Boolean): Result<Unit> {
        if (videoId.isBlank()) {
            return Result.error(IllegalArgumentException("视频ID不能为空"))
        }
        
        return videoRepository.likeVideo(videoId, isLiked)
    }
}