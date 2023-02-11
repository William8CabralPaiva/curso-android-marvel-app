package com.example.core.usecase

import com.example.core.data.repository.CharacterRepository
import com.example.core.usecase.base.ResultStatus
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharactersCategoryUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var getCharactersCategoryUseCase: GetCharactersCategoryUseCase

    @Mock
    private lateinit var repository: CharacterRepository

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    @Before
    fun setup() {
        getCharactersCategoryUseCase = GetCharactersCategoryUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider//thread     de test
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            //Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenReturn(events)
            //Act
            val result = getCharactersCategoryUseCase.invoke(
                GetCharactersCategoryUseCase.GetCategoriesParams(
                    character.id
                )
            )
            //Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
            //Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenAnswer{ throw Throwable()}
            //Act
            val result = getCharactersCategoryUseCase.invoke(
                GetCharactersCategoryUseCase.GetCategoriesParams(
                    character.id
                )
            )
            //Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }

    @Test
    fun `should return Error from ResultStatus when get comics request returns error`() =
        runTest {
            //Arrange
            whenever(repository.getComics(character.id)).thenAnswer{ throw Throwable()}
            //Act
            val result = getCharactersCategoryUseCase.invoke(
                GetCharactersCategoryUseCase.GetCategoriesParams(
                    character.id
                )
            )
            //Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }

}