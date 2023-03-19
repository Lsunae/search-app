package com.lsunae.search_app.data.repository

import com.lsunae.search_app.data.model.ImageSearchResponse
import retrofit2.Response

interface ImageSearchRepository {
    suspend fun searchImage(query: String): Response<ImageSearchResponse>
}