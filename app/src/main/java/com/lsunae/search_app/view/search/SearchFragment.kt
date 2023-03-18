package com.lsunae.search_app.view.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsunae.search_app.R
import com.lsunae.search_app.databinding.FragmentSearchBinding
import com.lsunae.search_app.view.base.BaseFragment
import com.lsunae.search_app.view.search.adapter.SearchResultAdapter

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setAdapter() {
        searchResultAdapter = SearchResultAdapter()
        binding.rvSearchResult.apply {
            // 기존 코드
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchResultAdapter
        }
    }
}