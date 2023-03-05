package com.example.core.usecase

import com.example.core.data.mapper.SortingMapper
import com.example.core.data.repository.StorageRepository
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharactersSortingUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<Pair<String, String>>

}

class GetCharactersSortingUseCaseImpl @Inject constructor(
    private val repository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val dispatcher: CoroutinesDispatchers
) : FlowUseCase<Unit, Pair<String, String>>(), GetCharactersSortingUseCase {

    //flow função suspensa que devolve varios valores, possivel emitir mais de um valor
    override suspend fun createFlowObservable(params: Unit): Flow<Pair<String, String>> {
        return withContext(dispatcher.io()) {
            repository.sorting.map {
                sortingMapper.mapToPair(it)
            }
        }

    }

}