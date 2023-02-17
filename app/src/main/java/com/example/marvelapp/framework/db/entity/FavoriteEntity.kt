package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.DBConstants
import com.example.core.domain.model.Character

@Entity(tableName = DBConstants.FAVORITES_TABLE)
data class FavoriteEntity(
    @PrimaryKey//todo autoincrement = autogenerate // por default vem como falso
    @ColumnInfo(name = DBConstants.FAVORITES_COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = DBConstants.FAVORITES_COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = DBConstants.FAVORITES_COLUMN_IMAGE)
    val imageUrl: String
)

fun List<FavoriteEntity>.listOfCharacterModel() = map {
    Character(it.id, it.name, it.imageUrl)
}

fun Character.toModelFavoriteEntity() = FavoriteEntity(this.id, this.name, this.imageUrl)