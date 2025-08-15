package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 根据分类获取视频用例
 */
class GetVideosByCategoryUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    
    /**
     * 执行根据分类获取视频
     * @param categoryId 分类ID
     */
    operator fun invoke(categoryId: String): Flow<Result<List<Video>>> {
        if (categoryId.isBlank()) {
            throw IllegalArgumentException("分类ID不能为空")
        }
        
        return videoRepository.getVideosByCategory(categoryId)
    }
}