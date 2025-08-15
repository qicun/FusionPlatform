package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import javax.inject.Inject

/**
 * 搜索视频用例
 */
class SearchVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    
    /**
     * 执行视频搜索
     * @param query 搜索关键词
     */
    suspend operator fun invoke(query: String): Result<List<Video>> {
        if (query.isBlank()) {
            return Result.error(IllegalArgumentException("搜索关键词不能为空"))
        }
        
        if (query.length < 2) {
            return Result.error(IllegalArgumentException("搜索关键词至少需要2个字符"))
        }
        
        return videoRepository.searchVideos(query.trim())
    }
}