package com.lsunae.search_app.data.repository.video

import android.util.Log
import com.lsunae.search_app.api.VideoSearchService
import com.lsunae.search_app.data.model.video.VideoSearchResponse
import retrofit2.Response
import javax.inject.Inject

class VideoSearchRepositoryImpl @Inject constructor(private val api: VideoSearchService) :
    VideoSearchRepository {
    override suspend fun searchVideo(
        query: String,
        page: Int,
        sort: String
    ): Response<VideoSearchResponse> {
        Log.d("[${javaClass.name}] api_video_ ", "query: $query, page: $page, sort: $sort")
        return api.searchVideo(query = query, page = page, size = 5, sort = sort)
    }
}