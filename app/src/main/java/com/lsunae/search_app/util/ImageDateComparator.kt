package com.lsunae.search_app.util

import com.lsunae.search_app.data.model.SearchResultData

class ImageDateComparator : Comparator<SearchResultData?> {
    override fun compare(p0: SearchResultData?, p1: SearchResultData?): Int {
        return java.lang.Long.valueOf(p0?.dateTime?.time ?: 0)
            .compareTo(p1?.dateTime?.time ?: 0)
    }
}