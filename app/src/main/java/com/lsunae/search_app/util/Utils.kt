package com.lsunae.search_app.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lsunae.search_app.data.model.SearchResultData
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun dateFormat(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return dateFormat.format(date)
    }

    fun saveFavoriteSharedPreferences(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val list = FavoriteDataManager.favoriteList
        val json = gson.toJson(list)

        editor.putString("imageStorage", json)
        editor.apply()
    }

    fun loadFavoriteSharedPreferences(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared preferences", MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("imageStorage", null)
        val type: Type = object : TypeToken<ArrayList<SearchResultData?>?>() {}.type

        FavoriteDataManager.favoriteList = gson.fromJson<Any>(json, type) as ArrayList<SearchResultData>

        if (FavoriteDataManager.favoriteList.isNullOrEmpty()) {
            FavoriteDataManager.favoriteList = ArrayList()
        }
    }
}