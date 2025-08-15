package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 搜索视频用例
 */
class SearchVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(query: String): Flow<Result<List<Video>>> {
        return videoRepository.searchVideos(query)
    }
}