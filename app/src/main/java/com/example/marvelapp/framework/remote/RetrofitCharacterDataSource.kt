package com.example.marvelapp.framework.remote

import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.response.CharacterWrapperResponse
import com.example.marvelapp.framework.network.MarvelApi
import javax.inject.Inject

class RetrofitCharacterDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharacterRemoteDataSource<CharacterWrapperResponse> {

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterWrapperResponse {
        return marvelApi.getCharacters(queries)
    }
}