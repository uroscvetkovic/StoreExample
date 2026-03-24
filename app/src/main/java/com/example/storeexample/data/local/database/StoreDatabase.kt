package com.example.storeexample.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storeexample.data.local.dao.FavoriteProductDao
import com.example.storeexample.data.local.entity.FavoriteProduct

@Database(entities = [FavoriteProduct::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
