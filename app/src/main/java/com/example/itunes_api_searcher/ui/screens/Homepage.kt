package com.example.itunes_api_searcher.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.itunes_api_searcher.viewmodel.ItunesSearchViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomePage(
    vm: ItunesSearchViewModel,
    onNavigateToDetails: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchLimitText by remember { mutableStateOf("25") }
    var hasSearched by remember { mutableStateOf(false) }

    val results by vm.results.collectAsStateWithLifecycle(emptyList())
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Search iTunes Catalog",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Search music, movies, TV shows, and more",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search term") },
                        singleLine = true
                    )

                    TextField(
                        modifier = Modifier.width(90.dp),
                        value = searchLimitText,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) searchLimitText = input
                        },
                        label = { Text("Limit") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    enabled = searchQuery.isNotBlank() && !isLoading,
                    onClick = {
                        val limit = searchLimitText.toIntOrNull() ?: 25
                        vm.search(searchQuery, limit)
                        hasSearched = true
                    }
                ) {
                    Text("Search")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
            }

            error != null -> {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            results.isEmpty() -> {
                Text(
                    text = if (hasSearched) "No results found" else "Search the Catalog to show various media!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(results) { item ->
                        ItemSearchResponseCard(
                            metaData = item,
                            onClick = {
                                vm.selectItem(item)
                                onNavigateToDetails()
                            }
                        )
                    }
                }
            }
        }
    }
}