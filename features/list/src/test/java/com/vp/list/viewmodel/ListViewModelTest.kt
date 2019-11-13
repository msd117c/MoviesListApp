package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.mock.Calls
import java.io.IOException
import java.util.*

class ListViewModelTest {
    @Rule
    @JvmField
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock<SearchService>(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure<SearchResponse>(IOException()))
        val listViewModel = ListViewModel(searchService)

        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify<Observer<SearchResult>>(mockObserver).onChanged(SearchResult.error())
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = mock<SearchService>(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.response<SearchResponse>(mock<SearchResponse>(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify<Observer<SearchResult>>(mockObserver).onChanged(SearchResult.inProgress())

        // Task 1: Check that state change correctly (as we mock the response, the returned data doesn't make sense)
        val items = ArrayList<ListItem>()
        verify<Observer<SearchResult>>(mockObserver).onChanged(SearchResult.success(items, items.size))
    }

}