package com.vp.favorites.model

import com.google.gson.annotations.SerializedName

data class ListItem(@SerializedName("Title")
                    var title: String? = null,
                    @SerializedName("Year")
                    var year: String? = null,
                    var imdbID: String = "",
                    @SerializedName("Poster")
                    var poster: String? = null)