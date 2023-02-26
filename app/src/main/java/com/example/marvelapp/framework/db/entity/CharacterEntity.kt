package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.DBConstants

@Entity(tableName = DBConstants.CHARACTERS_TABLE)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val autoId: Int = 0,
    @ColumnInfo(name = DBConstants.CHARACTERS_COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = DBConstants.CHARACTERS_COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = DBConstants.CHARACTERS_COLUMN_IMAGE_URL)
    val imageUrl: String
)