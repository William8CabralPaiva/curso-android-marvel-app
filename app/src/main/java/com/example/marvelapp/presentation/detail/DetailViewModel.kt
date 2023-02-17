package com.example.marvelapp.presentation.detail

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.GetCharactersCategoryUseCase
import com.example.core.usecase.base.AppCoroutinesDispatchers
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.marvelapp.R
import com.example.marvelapp.presentation.extensions.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharactersCategoryUseCase: GetCharactersCategoryUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    val categories =
        UiActionStateLiveData(coroutinesDispatchers.main(), getCharactersCategoryUseCase)

    val favorite = FavoriteUiActionStateLiveData(coroutinesDispatchers.main(), addFavoriteUseCase)

    init {
        favorite.setDefault()
    }


}