package com.vp.favorites.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(@SerializedName("Search")
                          var search: List<ListItem> = mutableListOf(),
                          var totalResults: Int = 0,
                          @SerializedName("Response")
                          var response: String) {
    private val POSITIVE_RESPONSE = "True"

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }
}