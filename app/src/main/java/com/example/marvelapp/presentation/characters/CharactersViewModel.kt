package com.example.marvelapp.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharacterUseCase
import com.example.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    fun charactersPagingData(query: String): Flow<PagingData<Character>> {
        return getCharacterUseCase(
            GetCharacterUseCase.GetCharacterParams(query, getPageConfig())
        ).cachedIn(viewModelScope)
    }

    private val action = MutableLiveData<Action>()
    //distinctuntilchange
    val state: LiveData<UiState> = action.switchMap { action ->
        when (action) {
            is Action.Search, Action.Sort -> {
                getCharacterUseCase(
                    GetCharacterUseCase.GetCharacterParams("", getPageConfig())
                ).cachedIn(viewModelScope).map {
                    UiState.SearchResult(it)
                }.asLiveData(coroutinesDispatchers.main())
            }
        }
    }

    private fun getPageConfig() = PagingConfig(20)

    fun searchCharacter(query: String = "") {
        action.value = Action.Search(query)
    }

    fun applySort() {
        action.value = Action.Sort
    }

    sealed class UiState {
        data class SearchResult(val dataCharacter: PagingData<Character>) : UiState()
    }

    sealed class Action {
        data class Search(val query: String) : Action()
        object Sort : Action()
    }
}