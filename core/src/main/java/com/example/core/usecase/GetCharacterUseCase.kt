package com.example.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) : PagingUseCase<GetCharacterUseCase.GetCharacterParams, Character>() {

    override fun createFlowObservable(params: GetCharacterParams): Flow<PagingData<Character>> {
        return Pager(config = params.pagingConfig) {
            characterRepository.getCharacters(params.query)
        }.flow
    }

    data class GetCharacterParams(
        val query: String,
        val pagingConfig: PagingConfig
    )
}