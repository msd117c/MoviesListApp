package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class ListItem(var id: Long,
                    @SerializedName("Title")
                    var title: String? = null,
                    @SerializedName("Year")
                    var year: String? = null,
                    // Task-6: Fix the issue by adding serializedName annotation (other option
                    // would be not obfuscate this model)
                    @SerializedName("imdbID")
                    var imdbID: String = "",
                    @SerializedName("Poster")
                    var poster: String? = null)