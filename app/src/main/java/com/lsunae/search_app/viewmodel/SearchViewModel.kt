package com.lsunae.search_app.viewmodel

import androidx.lifecycle.ViewModel
import com.lsunae.search_app.data.repository.ImageSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: ImageSearchRepository) :
    ViewModel() {

}