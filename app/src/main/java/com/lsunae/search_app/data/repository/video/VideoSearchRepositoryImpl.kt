package com.lsunae.search_app.data.repository.video

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
        return api.searchVideo(query = query, page = page, sort = sort)
    }
}