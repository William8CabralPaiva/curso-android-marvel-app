package com.example.core.usecase

import com.example.core.data.repository.CharacterRepository
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharactersCategoryUseCase {

    operator fun invoke(params: GetCategoriesParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCategoriesParams(val characterId: Int)

}

class GetCharactersCategoryUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository,
    private val dispatchers: CoroutinesDispatchers
) : GetCharactersCategoryUseCase,
    UseCase<GetCharactersCategoryUseCase.GetCategoriesParams, Pair<List<Comic>, List<Event>>>() {

    override suspend fun doWork(params: GetCharactersCategoryUseCase.GetCategoriesParams): ResultStatus<Pair<List<Comic>, List<Event>>> {

        return withContext(dispatchers.io()) {
            //usar o io para requisição
            // o computation para trabalho pesado , converter bitmap
            //todo função de forma assincrona só da sucesso dps que passa pelas duas
            val comicsDeferred = async { repository.getComics(params.characterId) }
            val eventDeferred = async { repository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }

}