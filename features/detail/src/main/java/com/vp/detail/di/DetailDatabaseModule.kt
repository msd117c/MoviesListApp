package com.vp.detail.di

import android.app.Application
import com.vp.detail.database.FavoritesRoomDatabase
import dagger.Module
import dagger.Provides

@Module
class DetailDatabaseModule {
    @Provides
    internal fun providesRoomDatabase(application: Application): FavoritesRoomDatabase = FavoritesRoomDatabase.getDatabase(application)
}

