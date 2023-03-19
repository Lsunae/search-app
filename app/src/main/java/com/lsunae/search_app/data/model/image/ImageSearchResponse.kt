package com.lsunae.search_app.data.model.image

import com.google.gson.annotations.SerializedName
import com.lsunae.search_app.data.model.MetaData

data class ImageSearchResponse(
    @SerializedName("meta")
    val metaData: MetaData?,

    @SerializedName("documents")
    var documents: MutableList<ImageData>?
)