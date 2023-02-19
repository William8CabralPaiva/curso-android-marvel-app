package com.example.marvelapp.presentation.common

interface ListItem {
    val key: Long

    fun areaItemsTheSame(other: ListItem) = this.key == other.key

    fun areaContentsTheSame(other: ListItem) = this == other
}