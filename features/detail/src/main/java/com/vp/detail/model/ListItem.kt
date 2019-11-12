package com.vp.detail.model

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
                    var imdbID: String = "",
                    @SerializedName("Poster")
                    var poster: String? = null)