package com.lsunae.search_app.data.model.video

import com.google.gson.annotations.SerializedName
import com.lsunae.search_app.data.model.MetaData

data class VideoSearchResponse (
    @SerializedName("meta")
    val metaData: MetaData?,

    @SerializedName("documents")
    var documents: MutableList<VideoData>?
)