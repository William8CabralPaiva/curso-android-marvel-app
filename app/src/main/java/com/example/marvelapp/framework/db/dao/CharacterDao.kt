package com.example.marvelapp.framework.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.DBConstants
import com.example.marvelapp.framework.db.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    // todo  implementation "androidx.room:room-paging:$room_version"//para funcionar paginação com o room

    @Query("SELECT * FROM ${DBConstants.CHARACTERS_TABLE}")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM ${DBConstants.CHARACTERS_TABLE}")
    suspend fun clearAll()

}