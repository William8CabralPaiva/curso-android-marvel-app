package com.example.marvelapp.framework.remote

import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.response.toCharacterModel
import com.example.core.data.response.toComicModel
import com.example.core.domain.model.CharacterPaging
import com.example.core.domain.model.Comic
import com.example.marvelapp.framework.network.MarvelApi
import javax.inject.Inject

class RetrofitCharacterDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharacterRemoteDataSource {

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging {
        val data = marvelApi.getCharacters(queries).data

        val characters = data.results.map {
            it.toCharacterModel()
        }

        return CharacterPaging(
            data.offset,
            data.total,
            characters
        )
    }

    override suspend fun fetchComics(characterId: Int): List<Comic> {

        return marvelApi.getComics(characterId).data.results.map {
            it.toComicModel()
        }

    }
}