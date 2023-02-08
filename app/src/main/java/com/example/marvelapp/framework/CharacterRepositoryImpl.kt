package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.repository.CharacterRepository
import com.example.core.domain.model.Character
import com.example.core.domain.model.Comic
import com.example.marvelapp.framework.paging.CharacterPagingSource
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharacterPagingSource(remoteDataSource, query)
    }

    override suspend fun getComics(characterId: Int): List<Comic> {
        return remoteDataSource.fetchComics(characterId )
    }

}