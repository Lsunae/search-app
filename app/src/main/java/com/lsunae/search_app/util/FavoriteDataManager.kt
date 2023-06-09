package com.lsunae.search_app.util

import com.lsunae.search_app.data.model.SearchResultData

object FavoriteDataManager {
    var favoriteList = arrayListOf<SearchResultData>()
    var isFavoriteListChange = false

    fun addFavoriteImage(data: SearchResultData) {
        this.favoriteList.add(0, data)
    }

    fun removeFavoriteImage(resultData: SearchResultData) {
        this.favoriteList.remove(resultData)
    }

    fun removeFavoriteImage(position: Int) {
        this.favoriteList.removeAt(position)
    }
}