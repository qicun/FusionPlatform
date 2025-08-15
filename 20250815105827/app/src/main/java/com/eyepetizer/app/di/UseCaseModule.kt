package com.eyepetizer.app.di

import com.eyepetizer.app.domain.repository.VideoRepository
import com.eyepetizer.app.domain.usecase.video.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 用例依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideGetFeedVideosUseCase(
        videoRepository: VideoRepository
    ): GetFeedVideosUseCase {
        return GetFeedVideosUseCase(videoRepository)
    }
    
    @Provides
    @Singleton
    fun provideGetVideoDetailUseCase(
        videoRepository: VideoRepository
    ): GetVideoDetailUseCase {
        return GetVideoDetailUseCase(videoRepository)
    }
    
    @Provides
    @Singleton
    fun provideSearchVideosUseCase(
        videoRepository: VideoRepository
    ): SearchVideosUseCase {
        return SearchVideosUseCase(videoRepository)
    }
    
    @Provides
    @Singleton
    fun provideLikeVideoUseCase(
        videoRepository: VideoRepository
    ): LikeVideoUseCase {
        return LikeVideoUseCase(videoRepository)
    }
    
    @Provides
    @Singleton
    fun provideGetVideosByCategoryUseCase(
        videoRepository: VideoRepository
    ): GetVideosByCategoryUseCase {
        return GetVideosByCategoryUseCase(videoRepository)
    }
}