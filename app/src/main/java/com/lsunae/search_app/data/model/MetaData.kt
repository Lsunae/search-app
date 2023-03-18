package com.lsunae.search_app.data.model

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int?,

    @SerializedName("pageable_count")
    val pageable_count: Int?,

    @SerializedName("is_end")
    val isEnd: Boolean?
)