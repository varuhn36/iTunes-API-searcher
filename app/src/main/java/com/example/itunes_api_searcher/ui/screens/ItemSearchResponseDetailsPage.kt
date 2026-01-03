package com.example.itunes_api_searcher.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.itunes_api_searcher.R
import com.example.itunes_api_searcher.data.model.ItunesItemMetaData
import com.example.itunes_api_searcher.utils.ImageLoading.loadImage
import com.example.itunes_api_searcher.viewmodel.ItunesSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchResponseDetailsPage(
    onBack: () -> Unit,
    vm: ItunesSearchViewModel
) {
    val item by vm.selectedItem.collectAsState()
    val error by vm.detailsError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        vm.clearSelectedItem()
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                error != null -> {
                    ErrorState(
                        message = error!!,
                        onBack = {
                            vm.clearSelectedItem()
                            onBack()
                        }
                    )
                }

                item == null -> {
                    CircularProgressIndicator()
                }

                else -> {
                    DetailsContent(item!!)
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onBack) {
            Text("Go Back")
        }
    }
}

@Composable
private fun DetailsContent(metaData: ItunesItemMetaData) {
    var isImageLoading by remember { mutableStateOf(true) }

    val image by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = metaData.artworkUrl100
    ) {
        isImageLoading = true
        value = loadImage(highResArtwork(metaData.artworkUrl100))
        isImageLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isImageLoading -> CircularProgressIndicator()
            image != null -> {
                Image(
                    bitmap = image!!,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 240.dp, max = 360.dp),
                    contentScale = ContentScale.Fit
                )
            }
            else -> {
                Image(
                    painter = painterResource(R.drawable.itunes_card_artwork_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = metaData.trackName
                    ?: metaData.collectionName
                    ?: "Unknown Title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = metaData.artistName ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            DetailRow("Type", metaData.kind)
            DetailRow("Genre", metaData.primaryGenreName)
            DetailRow("Release Date", metaData.releaseDate)
            DetailRow(
                "Price",
                metaData.trackPrice?.let { "${it} ${metaData.currency}" }
            )

            val description =
                metaData.longDescription
                    ?: metaData.description
                    ?: metaData.shortDescription

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description", style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String?
) {
    if (value.isNullOrBlank()) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun highResArtwork(url: String?): String? {
    return url?.replace("100x100", "600x600")
}


