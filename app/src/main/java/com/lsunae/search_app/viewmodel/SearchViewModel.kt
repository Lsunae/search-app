package com.lsunae.search_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.data.model.image.ImageData
import com.lsunae.search_app.data.model.video.VideoData
import com.lsunae.search_app.data.repository.image.ImageSearchRepository
import com.lsunae.search_app.data.repository.video.VideoSearchRepository
import com.lsunae.search_app.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _resultList = MutableLiveData<List<SearchResultData>?>()
    val resultList: LiveData<List<SearchResultData>?> get() = _resultList
    var resultData = mutableListOf<SearchResultData>()

    val imageTotalCount = MutableLiveData<Int?>()
    val videoTotalCount = MutableLiveData<Int?>()

    fun searchImage(query: String) {
        viewModelScope.launch {
            isImageLoading = false
            val response = imageRepository.searchImage(query)
            if (response.isSuccessful) {
                imageTotalCount.value = response.body()?.metaData?.totalCount
                _imageList.value = response.body()?.documents

                _imageList.value?.forEach {
                    val image = SearchResultData(
                        thumbnail = it.thumbnail_url,
                        dateTime = it.datetime,
                        urlType = Constants.IMAGE
                    )
                    resultData.add(image)
                }
            }
            isImageLoading = true
            searchResultData()
        }
    }

    fun searchVideo(query: String) {
        viewModelScope.launch {
            isVideoLoading = false
            val response = videoRepository.searchVideo(query)
            if (response.isSuccessful) {
                videoTotalCount.value = response.body()?.metaData?.totalCount
                _videoList.value = response.body()?.documents

                _videoList.value?.forEach {
                    val image = SearchResultData(
                        thumbnail = it.thumbnail,
                        dateTime = it.datetime,
                        urlType = Constants.VIDEO
                    )
                    resultData.add(image)
                }
            }
            isVideoLoading = true
            searchResultData()
        }
    }

    fun searchResultData() {
        if (isImageLoading && isVideoLoading) {
            _resultList.value = resultData
        }
    }
}