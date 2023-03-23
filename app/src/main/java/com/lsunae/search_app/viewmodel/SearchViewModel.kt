package com.lsunae.search_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsunae.search_app.data.model.MetaData
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.data.model.image.ImageData
import com.lsunae.search_app.data.model.video.VideoData
import com.lsunae.search_app.data.repository.image.ImageSearchRepository
import com.lsunae.search_app.data.repository.video.VideoSearchRepository
import com.lsunae.search_app.util.Constants
import com.lsunae.search_app.util.Constants.Companion.IMAGE_MAX_PAGE
import com.lsunae.search_app.util.Constants.Companion.RECENCY
import com.lsunae.search_app.util.Constants.Companion.VIDEO_MAX_PAGE
import com.lsunae.search_app.util.ImageDateComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imageRepository: ImageSearchRepository,
    private val videoRepository: VideoSearchRepository
) :
    ViewModel() {
    private val _imageList = MutableLiveData<List<ImageData>?>()
    private val _videoList = MutableLiveData<List<VideoData>?>()

    private var resultData = mutableListOf<SearchResultData>()
    private val _resultList = MutableLiveData<List<SearchResultData>?>()
    val resultList: LiveData<List<SearchResultData>?> get() = _resultList

    private val _imageMetadata = MutableLiveData<MetaData?>()
    val imageMetadata: LiveData<MetaData?> get() = _imageMetadata

    private val _videoMetadata = MutableLiveData<MetaData?>()
    val videoMetadata: LiveData<MetaData?> get() = _videoMetadata

    private var isImageLoading = false
    private var isVideoLoading = false

    fun searchKeyword(
        query: String,
        imagePage: Int,
        videoPage: Int,
        imageIsEnd: Boolean,
        videoIsEnd: Boolean
    ) {
        viewModelScope.launch {
            isImageLoading = false
            isVideoLoading = false

            try {
                if (!imageIsEnd && imagePage <= IMAGE_MAX_PAGE) {
                    val imageResponse = imageRepository.searchImage(
                        query,
                        imagePage,
                        RECENCY
                    )
                    if (imageResponse.isSuccessful) {
                        println("vm_image_meta_ ${imageResponse.body()?.metaData}")
                        println("vm_image_isEnd_ ${imageResponse.body()?.metaData?.isEnd}")
                        _imageMetadata.value = imageResponse.body()?.metaData
                        _imageList.value = imageResponse.body()?.documents

                        _imageList.value?.forEach {
                            val image = SearchResultData(
                                thumbnail = it.thumbnail_url,
                                dateTime = it.datetime,
                                urlType = Constants.IMAGE
                            )
                            resultData.add(image)
                        }
                    } else {
                        Log.e(
                            "[${javaClass.name}] Image Search Error ",
                            "code: ${imageResponse.code()}, message: ${imageResponse.errorBody()?.string()}"
                        )
                    }
                    isImageLoading = true
                } else Log.i("[${javaClass.name}] ", "이미지 검색 결과 마지막 페이지 입니다.")

                if (!videoIsEnd && videoPage <= VIDEO_MAX_PAGE) {
                    val videoResponse = videoRepository.searchVideo(query, videoPage, RECENCY)
                    if (videoResponse.isSuccessful) {
                        println("vm_video_meta_ ${videoResponse.body()?.metaData}")
                        println("vm_video_isEnd_ ${videoResponse.body()?.metaData?.isEnd}")
                        _videoMetadata.value = videoResponse.body()?.metaData
                        _videoList.value = videoResponse.body()?.documents

                        _videoList.value?.forEach {
                            val image = SearchResultData(
                                thumbnail = it.thumbnail,
                                dateTime = it.datetime,
                                urlType = Constants.VIDEO
                            )
                            resultData.add(image)
                        }
                    } else {
                        Log.e(
                            "[${javaClass.name}] Video Search Error ",
                            "code: ${videoResponse.code()}, message: ${videoResponse.body()?.metaData}, message2: ${videoResponse.errorBody()?.string()}"
                        )
                    }
                    isVideoLoading = true
                }
            } catch (exception: IOException) {
                Log.e("[${javaClass.name}] Exception ", "${exception.message}")
            }
            searchResultData()
        }
    }

    private fun searchResultData() {
        Collections.sort(resultData, ImageDateComparator().reversed())
        _resultList.value = resultData
        resultData.clear()
    }
}