package com.lsunae.search_app.data.repository

import com.lsunae.search_app.api.ImageSearchService
import com.lsunae.search_app.data.model.ImageSearchResponse
import retrofit2.Response
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(private val api: ImageSearchService) :
    ImageSearchRepository {
    override suspend fun searchImage(query: String, sort: String): Response<ImageSearchResponse> {
        return api.searchImage(query = query, sort = sort, page = 1, size = 5)
    }
}