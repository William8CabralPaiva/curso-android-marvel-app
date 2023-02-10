package com.example.marvelapp.framework.network

import com.example.core.data.response.CharacterResponse
import com.example.core.data.response.ComicResponse
import com.example.core.data.response.DataWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MarvelApi {

    @GET("characters")
    suspend fun getCharacters(
        @QueryMap
        queries: Map<String, String>
    ): DataWrapperResponse<CharacterResponse>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId")
        characterId: Int,
    ): DataWrapperResponse<ComicResponse>

}