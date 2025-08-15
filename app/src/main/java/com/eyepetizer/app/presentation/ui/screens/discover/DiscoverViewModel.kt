package com.eyepetizer.app.presentation.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepetizer.app.domain.model.Category
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.usecase.video.GetVideosByCategoryUseCase
import com.eyepetizer.app.domain.usecase.video.SearchVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getVideosByCategoryUseCase: GetVideosByCategoryUseCase,
    private val searchVideosUseCase: SearchVideosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    init {
        loadCategories()
        loadFeaturedVideos()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCategories = true)
            
            try {
                // 模拟分类数据
                val categories = listOf(
                    Category(id = "1", name = "推荐", description = "为你推荐", icon = "🔥"),
                    Category(id = "2", name = "科技", description = "科技前沿", icon = "💻"),
                    Category(id = "3", name = "美食", description = "美食天下", icon = "🍜"),
                    Category(id = "4", name = "旅行", description = "世界那么大", icon = "✈️"),
                    Category(id = "5", name = "音乐", description = "音乐无界", icon = "🎵"),
                    Category(id = "6", name = "运动", description = "运动健康", icon = "⚽"),
                    Category(id = "7", name = "时尚", description = "时尚潮流", icon = "👗"),
                    Category(id = "8", name = "游戏", description = "游戏世界", icon = "🎮")
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoadingCategories = false,
                    categories = categories
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingCategories = false,
                    error = e.message ?: "加载分类失败"
                )
            }
        }
    }

    private fun loadFeaturedVideos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingVideos = true)
            
            try {
                // 模拟精选视频数据
                val videos = generateMockVideos()
                
                _uiState.value = _uiState.value.copy(
                    isLoadingVideos = false,
                    featuredVideos = videos
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingVideos = false,
                    error = e.message ?: "加载视频失败"
                )
            }
        }
    }

    fun loadVideosByCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingVideos = true,
                selectedCategoryId = categoryId
            )
            
            try {
                // 这里应该调用实际的UseCase
                // val result = getVideosByCategoryUseCase(categoryId)
                
                // 模拟根据分类加载视频
                val videos = generateMockVideosByCategory(categoryId)
                
                _uiState.value = _uiState.value.copy(
                    isLoadingVideos = false,
                    featuredVideos = videos
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingVideos = false,
                    error = e.message ?: "加载分类视频失败"
                )
            }
        }
    }

    fun searchVideos(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchUiState()
            return
        }

        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(
                isLoading = true,
                query = query
            )
            
            try {
                // 这里应该调用实际的UseCase
                // val result = searchVideosUseCase(query)
                
                // 模拟搜索结果
                val searchResults = generateMockSearchResults(query)
                
                _searchState.value = _searchState.value.copy(
                    isLoading = false,
                    results = searchResults
                )
            } catch (e: Exception) {
                _searchState.value = _searchState.value.copy(
                    isLoading = false,
                    error = e.message ?: "搜索失败"
                )
            }
        }
    }

    private fun generateMockVideos(): List<Video> {
        return listOf(
            Video(
                id = "featured_1",
                title = "2024年度科技回顾",
                description = "回顾2024年最重要的科技发展",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=1",
                duration = 480,
                category = "科技",
                authorId = "tech_author",
                authorName = "科技观察者",
                authorIcon = "https://picsum.photos/100/100?random=1",
                playCount = 125000,
                likeCount = 8500,
                shareCount = 1200,
                commentCount = 650,
                createTime = System.currentTimeMillis() - 86400000
            ),
            Video(
                id = "featured_2",
                title = "日式料理制作秘籍",
                description = "学会正宗的日式料理制作方法",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=2",
                duration = 720,
                category = "美食",
                authorId = "food_author",
                authorName = "料理大师",
                authorIcon = "https://picsum.photos/100/100?random=2",
                playCount = 89000,
                likeCount = 6200,
                shareCount = 890,
                commentCount = 420,
                createTime = System.currentTimeMillis() - 172800000
            )
        )
    }

    private fun generateMockVideosByCategory(categoryId: String): List<Video> {
        val categoryName = _uiState.value.categories.find { it.id == categoryId }?.name ?: "推荐"
        
        return listOf(
            Video(
                id = "category_${categoryId}_1",
                title = "${categoryName}精选内容 #1",
                description = "这是${categoryName}分类下的精选视频内容",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=${categoryId}1",
                duration = 360,
                category = categoryName,
                authorId = "author_$categoryId",
                authorName = "${categoryName}达人",
                authorIcon = "https://picsum.photos/100/100?random=${categoryId}1",
                playCount = 45000,
                likeCount = 3200,
                shareCount = 480,
                commentCount = 280,
                createTime = System.currentTimeMillis() - 86400000
            ),
            Video(
                id = "category_${categoryId}_2",
                title = "${categoryName}精选内容 #2",
                description = "更多${categoryName}相关的优质内容",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=${categoryId}2",
                duration = 540,
                category = categoryName,
                authorId = "author_$categoryId",
                authorName = "${categoryName}专家",
                authorIcon = "https://picsum.photos/100/100?random=${categoryId}2",
                playCount = 32000,
                likeCount = 2100,
                shareCount = 320,
                commentCount = 190,
                createTime = System.currentTimeMillis() - 172800000
            )
        )
    }

    private fun generateMockSearchResults(query: String): List<Video> {
        return listOf(
            Video(
                id = "search_1",
                title = "搜索结果：$query 相关内容",
                description = "这是关于 $query 的搜索结果",
                playUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                coverUrl = "https://picsum.photos/400/225?random=search1",
                duration = 300,
                category = "搜索",
                authorId = "search_author",
                authorName = "内容创作者",
                authorIcon = "https://picsum.photos/100/100?random=search1",
                playCount = 15000,
                likeCount = 1200,
                shareCount = 180,
                commentCount = 95,
                createTime = System.currentTimeMillis() - 86400000
            )
        )
    }
}

data class DiscoverUiState(
    val isLoadingCategories: Boolean = false,
    val isLoadingVideos: Boolean = false,
    val categories: List<Category> = emptyList(),
    val featuredVideos: List<Video> = emptyList(),
    val selectedCategoryId: String? = null,
    val error: String? = null
)

data class SearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val results: List<Video> = emptyList(),
    val error: String? = null
)