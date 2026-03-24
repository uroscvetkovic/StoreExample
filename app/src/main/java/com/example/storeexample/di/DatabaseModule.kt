package com.example.storeexample.di

import android.content.Context
import androidx.room.Room
import com.example.storeexample.data.local.dao.FavoriteProductDao
import com.example.storeexample.data.local.database.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StoreDatabase =
        Room.databaseBuilder(context, StoreDatabase::class.java, "store_db").build()

    @Provides
    fun provideFavoriteProductDao(db: StoreDatabase): FavoriteProductDao =
        db.favoriteProductDao()
}
