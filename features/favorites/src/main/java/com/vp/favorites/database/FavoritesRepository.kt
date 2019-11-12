package com.vp.favorites.database

import androidx.lifecycle.LiveData
import com.vp.favorites.model.ListItem
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val favoritesRoomDatabase: FavoritesRoomDatabase) {

    suspend fun insertFavorite(movie: ListItem) {
        favoritesRoomDatabase.favoritesDao().insertFavorite(movie)
    }

    suspend fun removeFavorite(movieId: String) {
        favoritesRoomDatabase.favoritesDao().deleteFavorite(movieId)
    }

    suspend fun getFavorite(movieId: String): ListItem?  = favoritesRoomDatabase.favoritesDao().getFavorite(movieId)[0]

    fun getAllFavorites(): LiveData<List<ListItem>> = favoritesRoomDatabase.favoritesDao().getAllFavorites()

    fun getAllFavoritesByTitle(title: String): LiveData<List<ListItem>> = favoritesRoomDatabase.favoritesDao().getAllFavoritesByTitle(title)

    suspend fun removeAllFavorites() {
        favoritesRoomDatabase.favoritesDao().deleteAll()
    }

}