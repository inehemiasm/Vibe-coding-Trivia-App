package com.neo.trivia.ui.favorites

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.TechHubPostData
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.DiscoverySource
import com.neo.trivia.domain.repository.TechHubRepository
import com.neo.trivia.domain.usecase.GetFavoriteQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevHubViewModel @Inject constructor(
    private val getFavoriteQuestionsUseCase: GetFavoriteQuestionsUseCase,
    private val techHubRepository: TechHubRepository
) : BaseViewModel<DevHubUiState, DevHubIntent, DevHubEffect>(DevHubUiState()) {

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            
            combine(
                getFavoriteQuestionsUseCase(),
                techHubRepository.getSavedPosts()
            ) { questions, posts ->
                Pair(questions, posts)
            }.collect { (questions, posts) ->
                setState { 
                    copy(
                        questions = questions,
                        savedPosts = posts,
                        isLoading = false
                    )
                }
            }
        }
    }

    override suspend fun handleIntent(intent: DevHubIntent) {
        when (intent) {
            is DevHubIntent.LoadDevHub -> loadData()
            is DevHubIntent.ToggleFavorite -> {}
            is DevHubIntent.SwitchTab -> {
                setState { copy(selectedTab = intent.index) }
                if (intent.index == 2 && uiState.value.discoveryPosts.isEmpty()) {
                    fetchDiscovery(uiState.value.selectedSource)
                }
            }
            is DevHubIntent.SwitchDiscoverySource -> {
                setState { copy(selectedSource = intent.source, discoveryPosts = emptyList()) }
                fetchDiscovery(intent.source)
            }
            is DevHubIntent.SavePost -> {
                techHubRepository.savePost(intent.post)
            }
            is DevHubIntent.RemovePost -> {
                techHubRepository.removePost(intent.post)
            }
            is DevHubIntent.LoadDiscoveryPosts -> {
                fetchDiscovery(uiState.value.selectedSource)
            }
            is DevHubIntent.LoadPostDetails -> {
                loadPostDetails(intent.postId)
            }
        }
    }

    private fun fetchDiscovery(source: DiscoverySource) {
        viewModelScope.launch {
            setState { copy(isDiscoveryLoading = true) }
            val posts = techHubRepository.fetchDiscoverPosts(source)
            setState { copy(discoveryPosts = posts, isDiscoveryLoading = false) }
        }
    }

    private fun loadPostDetails(postId: String) {
        viewModelScope.launch {
            val post = techHubRepository.getPostById(postId)
            setState { copy(selectedPost = post) }
        }
    }
}

data class DevHubUiState(
    val questions: List<Question> = emptyList(),
    val savedPosts: List<TechHubPostData> = emptyList(),
    val discoveryPosts: List<TechHubPostData> = emptyList(),
    val selectedTab: Int = 0,
    val selectedSource: DiscoverySource = DiscoverySource.ANDROID_OFFICIAL,
    val selectedPost: TechHubPostData? = null,
    val isLoading: Boolean = false,
    val isDiscoveryLoading: Boolean = false,
) : UiState

sealed class DevHubIntent : UiIntent {
    object LoadDevHub : DevHubIntent()
    object LoadDiscoveryPosts : DevHubIntent()
    data class SwitchTab(val index: Int) : DevHubIntent()
    data class SwitchDiscoverySource(val source: DiscoverySource) : DevHubIntent()
    data class ToggleFavorite(val question: Question) : DevHubIntent()
    data class SavePost(val post: TechHubPostData) : DevHubIntent()
    data class RemovePost(val post: TechHubPostData) : DevHubIntent()
    data class LoadPostDetails(val postId: String) : DevHubIntent()
}

sealed class DevHubEffect : UiEffect
