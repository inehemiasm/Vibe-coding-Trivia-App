package com.neo.trivia.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.neo.design.cards.AppCard
import com.neo.trivia.R
import com.neo.trivia.domain.model.TechHubPostData
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.DiscoverySource
import com.neo.trivia.ui.MediumPostDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevHubScreen(
    viewModel: DevHubViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites_top_bar_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onIntent(DevHubIntent.SwitchTab(0)) },
                    text = { Text(stringResource(R.string.favorites_saved_tab)) }
                )
                Tab(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.onIntent(DevHubIntent.SwitchTab(1)) },
                    text = { Text(stringResource(R.string.favorites_discover_tab)) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val searchLabel =  stringResource(R.string.favorites_search_posts)
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.favorites_search_label, searchLabel)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    when (state.selectedTab) {
                        0 -> SavedPostsList(
                            state.savedPosts.filter { it.title.contains(searchQuery, ignoreCase = true) }, 
                            viewModel,
                            onPostClick = { navController.navigate(MediumPostDetailScreen(it.id)) }
                        )
                        1 -> DiscoverTab(state, viewModel, searchQuery, onPostClick = { navController.navigate(MediumPostDetailScreen(it.id)) })
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverTab(
    state: DevHubUiState,
    viewModel: DevHubViewModel,
    searchQuery: String,
    onPostClick: (TechHubPostData) -> Unit
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DiscoverySource.entries.toTypedArray()) { source ->
                FilterChip(
                    selected = state.selectedSource == source,
                    onClick = { viewModel.onIntent(DevHubIntent.SwitchDiscoverySource(source)) },
                    label = { Text(source.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isDiscoveryLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            DiscoverPostsList(
                posts = state.discoveryPosts.filter { it.title.contains(searchQuery, ignoreCase = true) },
                viewModel = viewModel,
                onPostClick = onPostClick
            )
        }
    }
}

@Composable
fun QuestionsList(questions: List<Question>, viewModel: DevHubViewModel) {
    if (questions.isEmpty()) {
        EmptyState(stringResource(R.string.favorites_no_questions))
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(questions) { question ->
                FavoriteQuestionCard(
                    question = question,
                    onFavoriteToggle = { viewModel.onIntent(DevHubIntent.ToggleFavorite(question)) },
                )
            }
        }
    }
}

@Composable
fun SavedPostsList(posts: List<TechHubPostData>, viewModel: DevHubViewModel, onPostClick: (TechHubPostData) -> Unit) {
    if (posts.isEmpty()) {
        EmptyState(stringResource(R.string.favorites_no_saved_posts))
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(posts) { post ->
                MediumPostCard(
                    post = post,
                    isSaved = true,
                    onSaveToggle = { viewModel.onIntent(DevHubIntent.RemovePost(post)) },
                    onClick = { onPostClick(post) }
                )
            }
        }
    }
}

@Composable
fun DiscoverPostsList(posts: List<TechHubPostData>, viewModel: DevHubViewModel, onPostClick: (TechHubPostData) -> Unit) {
    if (posts.isEmpty()) {
        EmptyState(stringResource(R.string.favorites_no_posts_found))
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(posts) { post ->
                MediumPostCard(
                    post = post,
                    isSaved = post.isSaved,
                    onSaveToggle = { 
                        if (post.isSaved) viewModel.onIntent(DevHubIntent.RemovePost(post))
                        else viewModel.onIntent(DevHubIntent.SavePost(post))
                    },
                    onClick = { onPostClick(post) }
                )
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun FavoriteQuestionCard(
    question: Question,
    onFavoriteToggle: () -> Unit,
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = question.question, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = question.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun MediumPostCard(
    post: TechHubPostData,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            if (post.imageUrl != null) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.favorites_by_author_format, post.author),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Clean preview content
                val previewContent = post.content.take(150) + "..."
                Text(
                    text = previewContent,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onSaveToggle) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.Download,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isSaved) stringResource(R.string.favorites_saved_tab) else stringResource(R.string.favorites_offline_label))
                    }
                }
            }
        }
    }
}
