package com.vp.favorites.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.favorites.model.ListItem

@Database(entities = [ListItem::class], version = 1, exportSchema = false)
abstract class FavoritesRoomDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: FavoritesRoomDatabase? = null

        fun getDatabase(context: Context): FavoritesRoomDatabase {
            instance?.let { nonNullInstance ->
                return nonNullInstance
            }
            return setDatabase(Room.databaseBuilder(context.applicationContext,
                    FavoritesRoomDatabase::class.java,
                    "favorites_database")
                    .build())
        }

        private fun setDatabase(favoritesRoomDatabase: FavoritesRoomDatabase): FavoritesRoomDatabase {
            instance = favoritesRoomDatabase
            return favoritesRoomDatabase
        }
    }

    abstract fun favoritesDao(): FavoritesDao

}