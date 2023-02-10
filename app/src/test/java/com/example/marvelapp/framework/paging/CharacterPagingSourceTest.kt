package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.response.DataWrapperResponse
import com.example.core.domain.model.Character
import com.example.marvelapp.factory.response.DataWrapperResponseFactory
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterPagingSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteDataSource: CharacterRemoteDataSource<DataWrapperResponse>

    private val characterFactory = CharacterFactory()

    private val characterWrapperResponse = DataWrapperResponseFactory().create()

    private lateinit var characterPagingSource: CharacterPagingSource

    @Before
    fun setup() {
        characterPagingSource = CharacterPagingSource(remoteDataSource, "")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should return a success load result when load is called`() = runTest {
        //todo refresh load 1 posição
        // append load infinito
        //prepend voltar

        //todo regra arrange act assert
        whenever(remoteDataSource.fetchCharacters(any())).thenReturn(characterWrapperResponse)

        val result = characterPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                false
            )
        )

        val expected = listOf(
            characterFactory.create(CharacterFactory.Hero.ThreeDMan),
            characterFactory.create(CharacterFactory.Hero.ABomb)
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = 20
            ),
            result
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should return a error load result when load is called`() = runTest {

        //arrange
        val exception = RuntimeException()
        whenever(remoteDataSource.fetchCharacters(any())).thenThrow(exception)

        //act
        val result = characterPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                false
            )
        )

        //assert
        assertEquals(
            PagingSource.LoadResult.Error<Int, Character>(exception),
            result
        )

    }
}
