package com.example.marvelapp.framework.di

import com.example.core.data.repository.CharacterRemoteDataSource
import com.example.core.data.repository.CharacterRepository
import com.example.marvelapp.framework.CharacterRepositoryImpl
import com.example.marvelapp.framework.remote.RetrofitCharacterDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharacterDataSource
    ): CharacterRemoteDataSource

}