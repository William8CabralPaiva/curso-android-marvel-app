package com.example.core.data.response

import com.google.gson.annotations.SerializedName

data class CharacterContainerResponse(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("results")
    val results: List<CharacterResponse>
)
