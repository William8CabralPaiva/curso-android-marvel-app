package com.example.marvelapp.framework.di

import com.example.core.usecase.GetCharacterUseCase
import com.example.core.usecase.GetCharacterUseCaseImpl
import com.example.core.usecase.GetCharactersCategoryUseCase
import com.example.core.usecase.GetCharactersCategoryUseCaseImpl
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
}