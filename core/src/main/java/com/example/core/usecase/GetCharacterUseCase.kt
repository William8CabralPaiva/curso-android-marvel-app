package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharacterUseCase.GetCharacterParams
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetCharacterUseCase {

    operator fun invoke(params: GetCharacterParams): Flow<PagingData<Character>>

    data class GetCharacterParams(
        val query: String,
        val pagingConfig: PagingConfig
    )
}

//todo esse impl foi criado para conseguir executar os testes
// não esquecer de extender do usecase
class GetCharacterUseCaseImpl @Inject constructor(
    private val characterRepository: CharacterRepository
) : PagingUseCase<GetCharacterParams, Character>(), GetCharacterUseCase {

    //flow função suspensa que devolve varios valores, possivel emitir mais de um valor
    override fun createFlowObservable(params: GetCharacterParams): Flow<PagingData<Character>> {
        return characterRepository.getCachedCharacters(params.query, params.pagingConfig)
    }


}