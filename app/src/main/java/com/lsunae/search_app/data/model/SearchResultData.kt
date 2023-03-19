package com.lsunae.search_app.data.model

import java.util.*

data class SearchResultData(
    val imageThumbnail: String?,
    val videoThumbnail: String?,
    val imageDateTime: Date?,
    val videoDateTime: Date?,
    val urlType: String
)
