package com.example.marvelapp.presentation.comicdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ComicDetailRoute(imageUrl: String) {
    ComicDetailScreen(imageUrl = imageUrl)
}

@Composable
fun ComicDetailScreen(imageUrl: String) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(text = imageUrl)
        }

    }
}

@Preview
@Composable
fun ComicDetailScreenPreview() {
    ComicDetailScreen(imageUrl = "TESTE")
}