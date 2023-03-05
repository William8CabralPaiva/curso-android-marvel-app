package com.example.marvelapp.framework.di


import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.AddFavoriteUseCaseImpl
import com.example.core.usecase.CheckFavoriteUseCase
import com.example.core.usecase.CheckFavoriteUseCaseImpl
import com.example.core.usecase.GetCharacterUseCase
import com.example.core.usecase.GetCharacterUseCaseImpl
import com.example.core.usecase.GetCharactersCategoryUseCase
import com.example.core.usecase.GetCharactersCategoryUseCaseImpl
import com.example.core.usecase.GetCharactersSortingUseCase
import com.example.core.usecase.GetCharactersSortingUseCaseImpl
import com.example.core.usecase.GetFavoritesUseCase
import com.example.core.usecase.GetFavoritesUseCaseImpl
import com.example.core.usecase.RemoveFavoriteUseCase
import com.example.core.usecase.RemoveFavoriteUseCaseImpl
import com.example.core.usecase.SaveCharacterSortingUseCase
import com.example.core.usecase.SaveCharacterSortingUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindGetCharactersUseCase(useCase: GetCharacterUseCaseImpl): GetCharacterUseCase

    //declarar os usecase
    @Binds
    fun bindGetComicsUseCase(useCase: GetCharactersCategoryUseCaseImpl): GetCharactersCategoryUseCase

    @Binds
    fun bindAddFavoriteUseCase(useCase: AddFavoriteUseCaseImpl): AddFavoriteUseCase

    @Binds
    fun bindCheckFavoriteUseCase(useCase: CheckFavoriteUseCaseImpl): CheckFavoriteUseCase

    @Binds
    fun bindDeleteCharacterUseCase(useCase: RemoveFavoriteUseCaseImpl): RemoveFavoriteUseCase

    @Binds
    fun bindGetFavoritesUseCase(useCase: GetFavoritesUseCaseImpl): GetFavoritesUseCase

    @Binds
    fun bindGetSortingCharacterUseCase(useCase: GetCharactersSortingUseCaseImpl): GetCharactersSortingUseCase

    @Binds
    fun bindGetCharactersSortingUseCase(useCase: SaveCharacterSortingUseCaseImpl): SaveCharacterSortingUseCase
}
