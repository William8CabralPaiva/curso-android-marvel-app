package com.example.marvelapp.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marvelapp.R
import com.example.marvelapp.presentation.detail.FavoriteUiActionStateLiveData

@Composable
fun FavoriteButton(
    state: FavoriteUiActionStateLiveData.UiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge.copy(
            topEnd = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        color = Color.White
    ) {
        if (state is FavoriteUiActionStateLiveData.UiState.Icon) {
            val icon = if (state.icon == R.drawable.ic_favorite_checked) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onClick() }
            )
        }

    }
}

@Preview
@Composable
fun FavoriteButtonPreview(modifier: Modifier = Modifier) {
    FavoriteButton(
        state = FavoriteUiActionStateLiveData.UiState.Icon(R.drawable.ic_no_favorites),
        onClick = {}
    )
}