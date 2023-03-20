package com.lsunae.search_app.data.repository.image

import com.lsunae.search_app.data.model.image.ImageSearchResponse
import retrofit2.Response

interface ImageSearchRepository {
    suspend fun searchImage(query: String, page: Int, sort: String): Response<ImageSearchResponse>
}