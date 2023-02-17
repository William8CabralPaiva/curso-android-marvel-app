package com.example.marvelapp.framework.di

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.data.repository.FavoritesRepository
import com.example.marvelapp.framework.FavoritesRepositoryImpl
import com.example.marvelapp.framework.local.RoomFavoritesDatasource
import com.example.marvelapp.framework.remote.RetrofitCharacterDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {

    @Binds
    fun bindFavoritesRepository(repository: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindLocalDataSource(
        dataSource: RoomFavoritesDatasource
    ): FavoritesLocalDataSource


}