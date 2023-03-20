package com.lsunae.search_app.data.repository.video

import com.lsunae.search_app.data.model.video.VideoSearchResponse
import retrofit2.Response

interface VideoSearchRepository {
    suspend fun searchVideo(query: String, page: Int, sort: String): Response<VideoSearchResponse>
}