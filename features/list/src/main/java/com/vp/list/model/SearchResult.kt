package com.vp.list.model

class SearchResult private constructor(val items: List<ListItem>, private val hasResponse: Boolean, val totalResult: Int) {

    fun hasResponse(): Boolean {
        return hasResponse
    }

    companion object {

        fun error(): SearchResult {
            return SearchResult(emptyList(), false, 0)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, true, totalResult)
        }
    }
}
