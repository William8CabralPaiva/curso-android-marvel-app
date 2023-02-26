package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.DBConstants

//todo PARA ter a chave, controlar a paginação
@Entity(tableName = DBConstants.REMOTE_KEYS_TABLE_NAME)
data class RemoteKey(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = DBConstants.REMOTE_KEYS_COLUMN_INFO_OFFSET)
    val nextOffset: Int?
)
