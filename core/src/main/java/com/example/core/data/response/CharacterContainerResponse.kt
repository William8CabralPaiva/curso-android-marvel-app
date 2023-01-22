package com.example.core.data.response

data class CharacterContainerResponse(
    val offset: Int,
    val total: Int,
    val results: List<CharacterResponse>
)
