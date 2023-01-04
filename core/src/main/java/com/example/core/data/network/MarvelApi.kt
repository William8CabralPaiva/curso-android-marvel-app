package com.example.core.data.network

import com.example.core.data.model.CharacterWrapper
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MarvelApi {

    @GET("characters")
    suspend fun getCharacters(
        @QueryMap
        queries: Map<String, String>
    ): CharacterWrapper

}