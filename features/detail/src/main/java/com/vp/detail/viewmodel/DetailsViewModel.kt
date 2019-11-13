package com.vp.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.detail.DetailActivity
import com.vp.detail.database.FavoritesRepository
import com.vp.detail.model.ListItem
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService,
                                           private val favoritesRepository: FavoritesRepository) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val favoriteState: MutableLiveData<Boolean> = MutableLiveData()

    private var item: ListItem? = null

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun favorite(): LiveData<Boolean> = favoriteState

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                item = ListItem(id = 0, imdbID = movieId)

                response?.body()?.let { detail ->
                    title.postValue(detail.title)
                    item?.title = detail.title
                    item?.year = detail.year
                    item?.poster = detail.poster
                }

                checkFavorite(item)
                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun checkFavorite(movie: ListItem?) {
        viewModelScope.launch {
            try {
                val item = favoritesRepository.getFavorite(movie?.imdbID ?: "")
                favoriteState.value = item != null
            } catch (e: Exception) {
                Log.d("ROOM", "Item not found")
                favoriteState.value = false
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val favorite = favoriteState.value
            favorite?.let { nonNullValue ->
                if (nonNullValue) {
                    favoritesRepository.removeFavorite(item?.imdbID ?: "")
                } else {
                    item?.let { nonNullItem ->
                        favoritesRepository.insertFavorite(nonNullItem)
                    }
                }
                checkFavorite(item)
            }
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}