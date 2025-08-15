package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import javax.inject.Inject

/**
 * 获取视频详情用例
 */
class GetVideoDetailUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    
    /**
     * 执行获取视频详情
     * @param videoId 视频ID
     */
    suspend operator fun invoke(videoId: String): Result<Video> {
        if (videoId.isBlank()) {
            return Result.error(IllegalArgumentException("视频ID不能为空"))
        }
        
        return videoRepository.getVideoDetail(videoId)
    }
}