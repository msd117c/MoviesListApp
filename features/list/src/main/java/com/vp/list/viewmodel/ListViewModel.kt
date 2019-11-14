package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject
internal constructor(private val searchService: SearchService) : ViewModel() {
    private val liveData = MutableLiveData<SearchResult>()

    private var currentTitle = ""
    private val aggregatedItems = mutableListOf<ListItem?>()
    private var currentTotalItems = 0

    fun observeMovies(): LiveData<SearchResult> {
        return liveData
    }

    fun searchMoviesByTitle(title: String?, page: Int) {
        title?.let { nonNullTitle ->
            if (page == 1 && nonNullTitle != currentTitle) {
                aggregatedItems.clear()
                currentTitle = nonNullTitle
                liveData.value = SearchResult.inProgress()
            } else if (page > 1 && aggregatedItems[aggregatedItems.size - 1] != null) {
                aggregatedItems.add(null)
                liveData.value = SearchResult.success(aggregatedItems, currentTotalItems)
            }

            searchService.search(nonNullTitle, page).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>,
                                        response: Response<SearchResponse>) {

                    val result = response.body()

                    if (page > 1) {
                        aggregatedItems.removeAt(aggregatedItems.size - 1)
                    }

                    if (result != null) {
                        aggregatedItems.addAll(result.getSearch())
                        // Task 1: Set the state when the process is successful (result != null)
                        liveData.value = SearchResult.success(aggregatedItems, result.totalResults)
                        currentTotalItems = result.totalResults
                    } else {
                        // Task 1: Set the state error when the process results
                        // in error (result == null)
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
