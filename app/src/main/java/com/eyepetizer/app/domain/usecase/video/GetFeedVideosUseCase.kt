package com.eyepetizer.app.domain.usecase.video

import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取推荐视频流用例
 */
class GetFeedVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    
    /**
     * 执行获取推荐视频流
     * @param refresh 是否强制刷新
     */
    operator fun invoke(refresh: Boolean = false): Flow<Result<List<Video>>> {
        return videoRepository.getFeedVideos(refresh)
    }
}