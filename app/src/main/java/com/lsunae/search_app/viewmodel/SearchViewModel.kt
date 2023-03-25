package com.lsunae.search_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsunae.search_app.data.model.MetaData
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.data.model.image.ImageData
import com.lsunae.search_app.data.model.image.ImageSearchResponse
import com.lsunae.search_app.data.model.video.VideoData
import com.lsunae.search_app.data.model.video.VideoSearchResponse
import com.lsunae.search_app.data.repository.image.ImageSearchRepository
import com.lsunae.search_app.data.repository.video.VideoSearchRepository
import com.lsunae.search_app.util.Constants
import com.lsunae.search_app.util.Constants.Companion.IMAGE_MAX_PAGE
import com.lsunae.search_app.util.Constants.Companion.RECENCY
import com.lsunae.search_app.util.Constants.Companion.VIDEO_MAX_PAGE
import com.lsunae.search_app.util.ImageDateComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
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

    private val _isMoreNotFount = MutableLiveData<Boolean>()
    val isMoreNotFount: LiveData<Boolean> get() = _isMoreNotFount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _currentKeyword = MutableLiveData<String>()
    val currentKeyword: LiveData<String> get() = _currentKeyword
    private var saveKeyword = ""

    private var isImageLoading = false
    private var isVideoLoading = false

    fun searchKeyword(
        query: String,
        imagePage: Int,
        videoPage: Int,
        imageIsEnd: Boolean,
        videoIsEnd: Boolean
    ) {
        if ((imagePage == 1 && videoPage == 1) && _currentKeyword.value != query) {
            saveKeyword = query
        } else _currentKeyword.value = saveKeyword

        viewModelScope.launch {
            isImageLoading = true
            isVideoLoading = true
            _isLoading.value = isImageLoading && isVideoLoading

            try {
                if (!imageIsEnd && imagePage <= IMAGE_MAX_PAGE) {
                    val imageResponse =
                        imageRepository.searchImage(saveKeyword, imagePage, RECENCY)
                    searchImageResult(imageResponse)
                } else {
                    isImageLoading = false
                    _isMoreNotFount.value = true
                    Log.i(
                        "[${javaClass.name}] ",
                        "This is the last page of the image search results."
                    )
                }

                if (!videoIsEnd && videoPage <= VIDEO_MAX_PAGE) {
                    val videoResponse =
                        videoRepository.searchVideo(saveKeyword, videoPage, RECENCY)
                    searchVideoResult(videoResponse)
                } else {
                    isVideoLoading = false
                    _isMoreNotFount.value = true
                    Log.i(
                        "[${javaClass.name}] ",
                        "This is the last page of the video search results."
                    )
                }
            } catch (exception: IOException) {
                isImageLoading = false
                isVideoLoading = false
                _isMoreNotFount.value = true
                Log.e("[${javaClass.name}] Exception ", "${exception.message}")
            }
            searchResultData()
        }
    }

    private fun searchImageResult(response: Response<ImageSearchResponse>) {
        if (response.isSuccessful) {
            _imageMetadata.value = response.body()?.metaData
            _imageList.value = response.body()?.documents

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
                "code: ${response.code()}, message: ${
                    response.errorBody()?.string()
                }"
            )
        }
        isImageLoading = false
        _isMoreNotFount.value = false
    }

    private fun searchVideoResult(response: Response<VideoSearchResponse>) {
        if (response.isSuccessful) {
            _videoMetadata.value = response.body()?.metaData
            _videoList.value = response.body()?.documents

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
                "code: ${response.code()}, message: ${
                    response.errorBody()?.string()
                }"
            )
        }
        isVideoLoading = false
        _isMoreNotFount.value = false
    }

    private fun searchResultData() {
        Collections.sort(resultData, ImageDateComparator().reversed())
        _resultList.value = resultData
        _isLoading.value = !(!isImageLoading && !isVideoLoading)
        resultData.clear()
    }
}