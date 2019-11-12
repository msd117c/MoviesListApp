package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.database.FavoritesRepository
import com.vp.favorites.model.ListItem
import javax.inject.Inject

class FavoriteViewModel @Inject
constructor(private val favoritesRepository: FavoritesRepository) : ViewModel() {

    private var listOfItems: LiveData<List<ListItem>> = favoritesRepository.getAllFavorites()
    private lateinit var filteredItems: LiveData<List<ListItem>>

    val source = MediatorLiveData<SearchResult>()

    private var searching = false
    private var query = ""

    init {
        source.addSource(listOfItems) { list ->
            source.postValue(SearchResult.success(list, list.size))
        }
    }

    fun resumeViewModel() {
        if (searching) {
            source.removeSource(filteredItems)
            filteredItems = favoritesRepository.getAllFavoritesByTitle(query)
            source.addSource(filteredItems) { list ->
                source.postValue(SearchResult.success(list, list.size))
            }
        } else {
            source.removeSource(listOfItems)
            listOfItems = favoritesRepository.getAllFavorites()
            source.addSource(listOfItems) { list ->
                source.postValue(SearchResult.success(list, list.size))
            }
        }
    }

    fun refreshList() {
        if (searching) {
            source.removeSource(filteredItems)
        } else {
            source.removeSource(listOfItems)
        }

        listOfItems = favoritesRepository.getAllFavorites()
        source.addSource(listOfItems) { list ->
            source.postValue(SearchResult.success(list, list.size))
        }
    }

    fun searchMoviesByTitle(title: String?) {
        if (searching) {
            source.removeSource(filteredItems)
        } else {
            source.removeSource(listOfItems)
        }
        source.postValue(SearchResult.inProgress())

        title?.let { nonNullTitle ->
            filteredItems = favoritesRepository.getAllFavoritesByTitle(nonNullTitle)
            source.addSource(filteredItems) { list ->
                source.postValue(SearchResult.success(list, list.size))
            }
            query = nonNullTitle
            searching = true
            return
        }
        source.postValue(SearchResult.error())
    }
}