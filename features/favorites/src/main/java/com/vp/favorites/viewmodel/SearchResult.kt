package com.vp.favorites.viewmodel

import com.vp.favorites.model.ListItem
import java.util.Objects

class SearchResult private constructor(val items: List<ListItem>,
                                       val totalResult: Int,
                                       val listState: ListState) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as SearchResult?
        return totalResult == that!!.totalResult &&
                items == that.items &&
                listState == that.listState
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {

        fun error(): SearchResult {
            return SearchResult(mutableListOf(), 0, ListState.ERROR)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, totalResult, ListState.LOADED)
        }

        fun inProgress(): SearchResult {
            return SearchResult(mutableListOf(), 0, ListState.IN_PROGRESS)
        }
    }
}
