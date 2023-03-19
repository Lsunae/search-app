package com.lsunae.search_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsunae.search_app.data.model.image.ImageData
import com.lsunae.search_app.data.model.video.VideoData
import com.lsunae.search_app.data.repository.image.ImageSearchRepository
import com.lsunae.search_app.data.repository.video.VideoSearchRepository
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

    val imageTotalCount = MutableLiveData<Int?>()
    val videoTotalCount = MutableLiveData<Int?>()

    fun searchImage(query: String) {
        viewModelScope.launch {
            val response = imageRepository.searchImage(query)
            if (response.isSuccessful) {
                imageTotalCount.value = response.body()?.metaData?.totalCount
                _imageList.value = response.body()?.documents
            }
        }
    }

    fun searchVideo(query: String) {
        viewModelScope.launch {
            val response = videoRepository.searchVideo(query)
            if (response.isSuccessful) {
                videoTotalCount.value = response.body()?.metaData?.totalCount
                _videoList.value = response.body()?.documents
            }
        }
    }
}