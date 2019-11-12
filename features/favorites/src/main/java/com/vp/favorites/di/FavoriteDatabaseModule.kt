package com.vp.favorites.di

import android.app.Application
import com.vp.favorites.database.FavoritesRoomDatabase
import dagger.Module
import dagger.Provides

@Module
class FavoriteDatabaseModule {
    @Provides
    internal fun providesRoomDatabase(application: Application): FavoritesRoomDatabase = FavoritesRoomDatabase.getDatabase(application)
}