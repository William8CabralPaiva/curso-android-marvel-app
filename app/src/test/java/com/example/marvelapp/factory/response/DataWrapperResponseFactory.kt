package com.example.marvelapp.factory.response

import com.example.core.data.response.CharacterContainerResponse
import com.example.core.data.response.CharacterResponse
import com.example.core.data.response.CharacterWrapperResponse
import com.example.core.data.response.ThumbnailResponse

class DataWrapperResponseFactory {

    fun create() = CharacterWrapperResponse(
        copyright = "",
        data = CharacterContainerResponse(
            offset = 0,
            total = 2,
            results = listOf(
                CharacterResponse(
                    id = "1011334",
                    name = "3-D Man",
                    thumbnail = ThumbnailResponse(
                        path = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
                        extension = "jpg"
                    )
                ),
                CharacterResponse(
                    id = "1017100",
                    name = "A-Bomb (HAS)",
                    thumbnail = ThumbnailResponse(
                        path = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16",
                        extension = "jpg"
                    )
                )
            )
        )
    )
}