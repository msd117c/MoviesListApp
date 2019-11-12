package com.vp.favorites.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorites_table")
data class ListItem(@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "id")
                    var id: Long,
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