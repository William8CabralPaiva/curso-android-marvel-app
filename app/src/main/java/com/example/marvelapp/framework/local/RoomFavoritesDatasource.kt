package com.example.marvelapp.framework.local

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.domain.model.Character
import com.example.marvelapp.framework.db.dao.FavoriteDao
import com.example.marvelapp.framework.db.entity.listOfCharacterModel
import com.example.marvelapp.framework.db.entity.toModelFavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDatasource @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesLocalDataSource {
    override fun getAll(): Flow<List<Character>> {
        return favoriteDao.loadFavorites().map {
            it.listOfCharacterModel()
        }
    }

    override suspend fun save(character: Character) {
        favoriteDao.insertFavorites(character.toModelFavoriteEntity())
    }

    override suspend fun delete(character: Character) {
        favoriteDao.deleteFavorites(character.toModelFavoriteEntity())
    }
}