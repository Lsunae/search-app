package com.lsunae.search_app.data.model.video

import com.google.gson.annotations.SerializedName
import java.util.*

data class VideoData(
    @SerializedName("title")
    val title: String?,
    @SerializedName("play_time")
    val play_time: Int?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("datetime")
    val datetime: Date?,
    @SerializedName("author")
    val author: String?
)