package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.usecase.GetCharacterUseCase
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime
import com.example.core.domain.model.Character as Character


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    //executa o before e o after automaticamente
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock//mockito não mocka instancia finaL, mocka apenas interface
    lateinit var getCharactersUseCase: GetCharacterUseCase

    private lateinit var characterViewModel: CharactersViewModel

    private val characterFactory = CharacterFactory()

    val list = listOf(
        characterFactory.create(CharacterFactory.Hero.ThreeDMan),
        characterFactory.create(CharacterFactory.Hero.ABomb),
    )

    private val pagingDataCharacters = PagingData.from(list)

    @Before
    fun setup() {
        characterViewModel = CharactersViewModel(getCharactersUseCase)
    }

    @Test
    fun `should validate paging data object values when calling charactersPagingData`() =
        runTest {//runBlockingTest

            whenever(
                getCharactersUseCase.invoke(any())
            ).thenReturn(flowOf(pagingDataCharacters))

            val result = characterViewModel.charactersPagingData("")

            assertNotNull(result)
        }

    @Test(expected = RuntimeException::class)
    fun `should throw an exception when calling to the use case returns an exception`() {
        runTest {
            whenever(getCharactersUseCase.invoke(any())).thenThrow(RuntimeException())

            characterViewModel.charactersPagingData("")

        }
    }
}


