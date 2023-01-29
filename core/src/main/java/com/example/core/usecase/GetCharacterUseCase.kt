package com.example.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.data.repository.CharacterRepository
import com.example.core.usecase.GetCharacterUseCase.GetCharacterParams
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.core.domain.model.Character as Character

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

    override fun createFlowObservable(params: GetCharacterParams): Flow<PagingData<Character>> {
        //precisa criar fora para o verify conseguir ler // GetCharactersUseCaseImplTest LINHA 53
        val pagingSource = characterRepository.getCharacters(params.query)
        return Pager(config = params.pagingConfig) {
           pagingSource
        }.flow
    }


}