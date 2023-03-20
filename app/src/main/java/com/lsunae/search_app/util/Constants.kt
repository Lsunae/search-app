package com.lsunae.search_app.util

import com.lsunae.search_app.util.ApiKey.Companion.REST_API_KEY

class Constants {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com"
        const val AUTH_HEADER = "KakaoAK $REST_API_KEY"

        const val IMAGE = "image"
        const val VIDEO = "video"

        const val RECENCY = "recency"   // 최신순
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}