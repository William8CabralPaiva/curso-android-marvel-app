package com.example.marvelapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marvelapp.framework.db.dao.FavoriteDao
import com.example.marvelapp.framework.db.entity.FavoriteEntity

//exportShema backup do banco para a gente ficar observando
@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}