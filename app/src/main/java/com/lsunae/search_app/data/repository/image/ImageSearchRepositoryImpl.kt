package com.lsunae.search_app.data.repository.image

import android.util.Log
import com.lsunae.search_app.api.ImageSearchService
import com.lsunae.search_app.data.model.image.ImageSearchResponse
import retrofit2.Response
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(private val api: ImageSearchService) :
    ImageSearchRepository {
    override suspend fun searchImage(
        query: String,
        page: Int,
        sort: String
    ): Response<ImageSearchResponse> {
        Log.d("[${javaClass.name}] api_image_ ", "query: $query, page: $page, sort: $sort")
        return api.searchImage(query = query, page = page, size = 5, sort = sort)
    }
}