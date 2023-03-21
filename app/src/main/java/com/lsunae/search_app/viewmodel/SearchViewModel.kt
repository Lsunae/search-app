package com.lsunae.search_app.viewmodel

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
import com.lsunae.search_app.util.Constants.Companion.RECENCY
import com.lsunae.search_app.util.ImageDateComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imageRepository: ImageSearchRepository,
    private val videoRepository: VideoSearchRepository
) :
    ViewModel() {
    private val _imageList = MutableLiveData<List<ImageData>?>()
    val imageList: LiveData<List<ImageData>?> get() = _imageList

    private val _videoList = MutableLiveData<List<VideoData>?>()
    val videoList: LiveData<List<VideoData>?> get() = _videoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var isImageLoading = false
    private var isVideoLoading = false

    var totalCount = MutableLiveData<Int>()

    private var resultData = mutableListOf<SearchResultData>()
    private val _resultList = MutableLiveData<List<SearchResultData>?>()
    val resultList: LiveData<List<SearchResultData>?> get() = _resultList

    private val _imageMetadata = MutableLiveData<MetaData?>()
    val imageMetadata: LiveData<MetaData?> get() = _imageMetadata

    private val _videoMetadata = MutableLiveData<MetaData?>()
    val videoMetadata: LiveData<MetaData?> get() = _videoMetadata

    var keyword = ""

    fun searchImage(query: String, page: Int) {
        if (keyword != query) {
            resultData.clear()
        }
        keyword = query

        println("image_page_ $page")
        viewModelScope.launch {
            println("test_imageList_size_ ${_imageList.value?.size}")
            isImageLoading = false
            val response = imageRepository.searchImage(query, page, RECENCY)
            if (response.isSuccessful) {
                _imageMetadata.value = response.body()?.metaData
                _imageList.value = response.body()?.documents
                println("test_image_isEnd_ ${response.body()?.metaData?.isEnd}")

                _imageList.value?.forEach {
                    val image = SearchResultData(
                        thumbnail = it.thumbnail_url,
                        dateTime = it.datetime,
                        urlType = Constants.IMAGE
                    )
                    resultData.add(image)
                }
                println("test_imageList_size_2_ ${_imageList.value?.size}")
            }
            isImageLoading = true
            searchResultData()
        }
    }

    fun searchVideo(query: String, page: Int) {
        println("video_page_ $page")
        viewModelScope.launch {
            println("test_videoList_size_ ${_videoList.value?.size}")
            isVideoLoading = false
            val response = videoRepository.searchVideo(query, page, RECENCY)
            if (response.isSuccessful) {
                _videoMetadata.value = response.body()?.metaData
                _videoList.value = response.body()?.documents
                println("test_video_isEnd_ ${response.body()?.metaData?.isEnd}")

                _videoList.value?.forEach {
                    val image = SearchResultData(
                        thumbnail = it.thumbnail,
                        dateTime = it.datetime,
                        urlType = Constants.VIDEO
                    )
                    resultData.add(image)
                }
                println("test_videoList_size_2_ ${_videoList.value?.size}")
            }
            isVideoLoading = true
            searchResultData()
        }
    }

    private fun searchResultData() {
        if (isImageLoading && isVideoLoading) {
            println("test_resultData_ ${resultData.size}")
            println("test_resultList_ ${_resultList.value?.size}")
            Collections.sort(resultData, ImageDateComparator().reversed())
            _resultList.value = resultData
            println("test_resultData_2_ ${resultData.size}")
            println("test_resultList_2_ ${_resultList.value?.size}")
            totalCount.value = resultData.size
            resultData.clear()
        }
    }
}