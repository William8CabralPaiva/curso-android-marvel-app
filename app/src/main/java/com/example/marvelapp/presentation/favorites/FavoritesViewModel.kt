package com.example.marvelapp.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.GetFavoritesUseCase
import com.example.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: GetFavoritesUseCase,
    private val dispatchers: CoroutinesDispatchers
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap { _action ->
        liveData(dispatchers.main()) {
            when (_action) {
                is Action.GetAll -> {
                    favoritesUseCase()
                        .catch {
                            emit(UiState.ShowEmpty)
                        }
                        .collect { list ->
                            val items = list.map {
                                FavoriteItem(
                                    it.id,
                                    it.name,
                                    it.imageUrl
                                )

                            }
                            val uiState = if (items.isNotEmpty()) {
                                UiState.ShowFavorite(items)
                            } else {
                                UiState.ShowEmpty
                            }

                            emit(uiState)

                        }
                }
            }
        }
    }

    fun getAll() {
        action.value = Action.GetAll
    }

    sealed class UiState {
        data class ShowFavorite(val favoriteItems: List<FavoriteItem>) : UiState()
        object ShowEmpty : UiState()
    }

    sealed class Action {
        object GetAll : Action()
    }
}