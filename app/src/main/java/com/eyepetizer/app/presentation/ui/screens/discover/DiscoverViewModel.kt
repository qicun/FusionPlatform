package com.eyepetizer.app.presentation.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepetizer.app.domain.model.Video
import com.eyepetizer.app.domain.usecase.video.GetVideosByCategoryUseCase
import com.eyepetizer.app.domain.usecase.video.SearchVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 发现页面ViewModel
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getVideosByCategoryUseCase: GetVideosByCategoryUseCase,
    private val searchVideosUseCase: SearchVideosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 预定义的视频分类
    private val categories = listOf(
        Category("all", "全部", "🎬"),
        Category("music", "音乐", "🎵"),
        Category("technology", "科技", "💻"),
        Category("lifestyle", "生活", "🌟"),
        Category("food", "美食", "🍽️"),
        Category("travel", "旅行", "✈️"),
        Category("sports", "运动", "⚽"),
        Category("education", "教育", "📚"),
        Category("entertainment", "娱乐", "🎭"),
        Category("fashion", "时尚", "👗")
    )

    init {
        loadCategories()
        loadVideosByCategory("all")
    }

    private fun loadCategories() {
        _uiState.update { currentState ->
            currentState.copy(
                categories = categories,
                selectedCategory = categories.first()
            )
        }
    }

    fun selectCategory(category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                isLoading = true,
                error = null
            )
        }
        loadVideosByCategory(category.id)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            // 如果搜索为空，显示当前分类的视频
            loadVideosByCategory(_uiState.value.selectedCategory.id)
        } else {
            searchVideos(query)
        }
    }

    private fun loadVideosByCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                getVideosByCategoryUseCase(categoryId).collect { videos ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            videos = videos,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message ?: "加载视频失败"
                    )
                }
            }
        }
    }

    private fun searchVideos(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                val searchResults = searchVideosUseCase(query)
                _uiState.update { currentState ->
                    currentState.copy(
                        videos = searchResults,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message ?: "搜索失败"
                    )
                }
            }
        }
    }

    fun refreshVideos() {
        val currentCategory = _uiState.value.selectedCategory.id
        val currentQuery = _searchQuery.value
        
        if (currentQuery.isBlank()) {
            loadVideosByCategory(currentCategory)
        } else {
            searchVideos(currentQuery)
        }
    }
}

/**
 * 发现页面UI状态
 */
data class DiscoverUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category = Category("all", "全部", "🎬"),
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 视频分类数据类
 */
data class Category(
    val id: String,
    val name: String,
    val icon: String
)