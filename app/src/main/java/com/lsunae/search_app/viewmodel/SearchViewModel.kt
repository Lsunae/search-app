package com.lsunae.search_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsunae.search_app.data.model.ImageData
import com.lsunae.search_app.data.repository.ImageSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: ImageSearchRepository) :
    ViewModel() {
    private val _imageList = MutableLiveData<List<ImageData>?>()
    val imageList: LiveData<List<ImageData>?> get() = _imageList

    val totalCount = MutableLiveData<Int?>()

        fun searchImage(query: String) {

            viewModelScope.launch {
                val response = repository.searchImage(query)
                if (response.isSuccessful) {
                totalCount.value = response.body()?.metaData?.totalCount
                    _imageList.value = response.body()?.documents
                }

            }
        }
}