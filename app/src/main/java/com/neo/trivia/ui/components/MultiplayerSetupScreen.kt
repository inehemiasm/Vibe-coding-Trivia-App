package com.neo.trivia.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neo.trivia.domain.model.Category
import com.neo.trivia.ui.FilterOptionChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplayerSetupScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToGame: (Int, Category?) -> Unit = { _, _ -> }
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var playerCount by remember { mutableStateOf(1) }
    var playerName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Multiplayer Setup") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Multiplayer Trivia",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "Select Category",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Category.values()) { category ->
                    FilterOptionChip(
                        text = category.displayName,
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Number of Players",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(4) { index ->
                    val count = index + 1
                    FilterOptionChip(
                        text = "$count",
                        selected = playerCount == count,
                        onClick = {
                            playerCount = count
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onNavigateToGame(playerCount, selectedCategory)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }

            TextButton(onClick = onNavigateBack) {
                Text("Cancel")
            }
        }
    }
}