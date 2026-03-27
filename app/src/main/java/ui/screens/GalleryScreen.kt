package com.openclaw.kanban.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Demo images from Picsum
val demoImages = (1..30).map { i ->
    "https://picsum.photos/seed/$i/400/400"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gallery") }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(demoImages) { url ->
                GalleryItem(
                    imageUrl = url,
                    onClick = { selectedImageUrl = url }
                )
            }
        }
        
        // Full screen image dialog
        selectedImageUrl?.let { url ->
            ImageViewerDialog(
                imageUrl = url,
                onDismiss = { selectedImageUrl = null }
            )
        }
    }
}

@Composable
fun GalleryItem(
    imageUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Placeholder with gradient
        val gradient = androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.secondaryContainer
            )
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            // Show image index as placeholder
            val index = demoImages.indexOf(imageUrl) + 1
            Text(
                text = "$index",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        // In production, uncomment this to load actual images:
        // AsyncImage(
        //     model = imageUrl,
        //     contentDescription = null,
        //     modifier = Modifier.fillMaxSize(),
        //     contentScale = ContentScale.Crop
        // )
    }
}

@Composable
fun ImageViewerDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
            
            // Image placeholder
            val index = demoImages.indexOf(imageUrl) + 1
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Image $index",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }
            
            // In production, uncomment this:
            // AsyncImage(
            //     model = imageUrl,
            //     contentDescription = null,
            //     modifier = Modifier.fillMaxSize(),
            //     contentScale = ContentScale.Fit
            // )
        }
    }
}
