package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharactersCategoryUseCase
import com.example.marvelapp.R
import com.example.marvelapp.presentation.extensions.watchStatus
import kotlin.coroutines.CoroutineContext

class UiActionStateLiveData(
    private val coroutineContext: CoroutineContext,
    private val getCharacterCategoriesUseCase: GetCharactersCategoryUseCase
) {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap {//transforma no uistate
        liveData(coroutineContext) {
            when (it) {
                is Action.Load -> {
                    getCharacterCategoriesUseCase(
                        GetCharactersCategoryUseCase.GetCategoriesParams(
                            it.characterId
                        )
                    ).watchStatus(
                        loading = {
                            emit(UiState.Loading)
                        },
                        success = { data ->
                            emit(successSaveFavorite(data))
                        },
                        error = {
                            emit(UiState.Error)
                        }
                    )
                }
            }
        }
    }

    fun load(characterId: Int) {
        action.value = Action.Load(characterId)
    }

    private fun successSaveFavorite(it: Pair<List<Comic>, List<Event>>): UiState {
        val detailParentList = mutableListOf<DetailParentVE>()

        val comics = it.first
        if (comics.isNotEmpty()) {
            comics.map { comic ->
                DetailChildVE(comic.id, comic.imageUrl)
            }.also {
                detailParentList.add(
                    DetailParentVE(R.string.details_comics_category, it)
                )
            }
        }

        val events = it.second
        if (events.isNotEmpty()) {
            events.map { event ->
                DetailChildVE(event.id, event.imageUrl)
            }.also {
                detailParentList.add(
                    DetailParentVE(R.string.details_events_category, it)
                )
            }
        }

        return if (detailParentList.isNotEmpty()) {
            UiState.Success(detailParentList)
        } else UiState.Empty
    }


    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }

    sealed class Action {
        data class Load(val characterId: Int) : Action()
    }

}