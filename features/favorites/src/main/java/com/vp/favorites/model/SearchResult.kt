package com.vp.favorites.model

import androidx.annotation.NonNull

class SearchResult(@NonNull val items: List<ListItem>,
                   val hasResponse: Boolean,
                   val totalResult: Int) {

    companion object {
        fun error(): SearchResult {
            return SearchResult(emptyList(), false, 0)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, true, totalResult)
        }
    }

}