package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.DBConstants
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao//avisa o room que aqui é o crud
interface FavoriteDao {

    //todo o flow n aceita suspend por isso tirei do loaf favorites //FavoritesDao
    // só posso usar em operação que vai ser usada uma unica vez
    @Query("SELECT * FROM ${DBConstants.FAVORITES_TABLE}")
    fun loadFavorites(): Flow<List<FavoriteEntity>>//tem que ter @entity a enidade

    @Insert(onConflict = OnConflictStrategy.REPLACE)//substituir se tiver id igual
    suspend fun insertFavorites(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorites(favoriteEntity: FavoriteEntity)
}