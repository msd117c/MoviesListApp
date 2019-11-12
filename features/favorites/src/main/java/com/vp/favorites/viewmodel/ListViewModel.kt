package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.model.ListItem
import com.vp.favorites.model.SearchResponse
import com.vp.favorites.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject
constructor(private val searchService: SearchService) : ViewModel() {
    private val liveData = MutableLiveData<SearchResult>()

    private var currentTitle = ""
    private val aggregatedItems = mutableListOf<ListItem>()

    fun observeMovies(): LiveData<SearchResult> {
        return liveData
    }

    fun searchMoviesByTitle(title: String?, page: Int) {
        title?.let { nonNullTitle ->
            if (page == 1 && nonNullTitle != currentTitle) {
                aggregatedItems.clear()
                currentTitle = nonNullTitle
                liveData.value = SearchResult.inProgress()
            }
            searchService.search(nonNullTitle, page).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {

                    val result = response.body()

                    if (result != null) {
                        aggregatedItems.addAll(result.search)
                        liveData.value = SearchResult.success(aggregatedItems, aggregatedItems.size)
                    } else {
                        liveData.value = SearchResult.error()
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    liveData.value = SearchResult.error()
                }
            })
            return
        }
        liveData.value = SearchResult.error()
    }
}