package com.example.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.data.repository.FavoritesRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.FlowUseCase
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<List<Character>>

}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
    private val dispatcher: CoroutinesDispatchers
) : FlowUseCase<Unit, List<Character>>(), GetFavoritesUseCase {

    //flow função suspensa que devolve varios valores, possivel emitir mais de um valor
    override suspend fun createFlowObservable(params: Unit): Flow<List<Character>> {
        return withContext(dispatcher.io()) {
            repository.getAllFavorites()
        }

    }


}