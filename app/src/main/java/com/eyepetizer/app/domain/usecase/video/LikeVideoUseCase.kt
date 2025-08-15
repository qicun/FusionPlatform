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
    suspend operator fun invoke(videoId: String): Result<Unit> {
        return try {
            // 这里应该调用实际的repository方法
            // videoRepository.likeVideo(videoId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "点赞失败")
        }
    }
}