package com.vp.detail.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.detail.model.ListItem

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: ListItem)

    @Query("DELETE FROM favorites_table WHERE imdbID = :imdbID")
    suspend fun deleteFavorite(imdbID: String)

    @Query("SELECT * FROM favorites_table WHERE imdbID = :imdbID")
    suspend fun getFavorite(imdbID: String): List<ListItem>

    @Query("SELECT * FROM favorites_table ORDER BY title ASC")
    fun getAllFavorites(): LiveData<List<ListItem>>

    @Query("SELECT * FROM favorites_table WHERE title LIKE '%' || :title || '%' ORDER BY title ASC")
    fun getAllFavoritesByTitle(title: String): LiveData<List<ListItem>>

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAll()
}