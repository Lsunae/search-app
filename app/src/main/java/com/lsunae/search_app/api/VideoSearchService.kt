package com.lsunae.search_app.api

import com.lsunae.search_app.data.model.video.VideoSearchResponse
import com.lsunae.search_app.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VideoSearchService {
    @GET("v2/search/vclip")
    suspend fun searchVideo(
        @Header("Authorization") apiKey: String = Constants.AUTH_HEADER,
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<VideoSearchResponse>
}