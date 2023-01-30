package com.example.core.data.response

import com.google.gson.annotations.SerializedName

data class CharacterWrapperResponse(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("data")
    val data: CharacterContainerResponse
)