package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.response.CharacterContainerResponse
import com.example.core.data.response.CharacterWrapperResponse
import com.example.core.data.response.toCharacterModel
import com.example.core.domain.model.Character

class CharacterPagingSource(
    private val remoteDataSource: CharacterRemoteDataSource<CharacterWrapperResponse>,
    private val query: String
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {

            val offset = params.key ?: 0
            val queries = hashMapOf(
                "offset" to offset.toString()
            )

            if (query.isNotEmpty()) {
                queries["nameStartWith"] = query
            }

            val response = remoteDataSource.fetchCharacters(queries)

            val data = response.data

            LoadResult.Page(
                data = data.results.map { it.toCharacterModel() },
                prevKey = null,
                nextKey = data.nextCharacters()
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    private fun CharacterContainerResponse.nextCharacters(): Int? {
        return if (this.offset < this.total) {
            this.offset + LIMIT
        } else {
            null
        }
    }

    companion object {
        const val LIMIT = 20
    }
}