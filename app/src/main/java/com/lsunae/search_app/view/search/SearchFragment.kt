package com.lsunae.search_app.view.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lsunae.search_app.R
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.FragmentSearchBinding
import com.lsunae.search_app.util.OnSingleClickListener
import com.lsunae.search_app.util.SingletonObject
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.util.hideKeyboard
import com.lsunae.search_app.view.base.BaseFragment
import com.lsunae.search_app.view.search.adapter.SearchResultAdapter
import com.lsunae.search_app.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var imagePage = 1
    private var videoPage = 1
    private var isNextPage = false
    private var imagePageIsEnd = false
    private var videoPageIsEnd = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setAdapter()
        searchResultAdapter.searchFragment = WeakReference(this)
        setListener()
        setViewModel()
    }

    private fun setupView() {
        binding.apply {
            incActionbar.tvTitle.text = "검색"
        }
    }

    private fun setListener() {
        binding.apply {
            ivSearch.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(v: View) {
                    searchKeyword()
                    etSearch.hideKeyboard()
                }
            })

            rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount ?: 0
                    val resultListCount = viewModel.totalCount.value ?: 0
                    println("f_itemTotalCount_ $imagePageIsEnd")
                    println("f_resultListCount_ $imagePageIsEnd")

                    if (itemTotalCount <= resultListCount && lastVisibleItemPosition + 1 == itemTotalCount) {
                        println("f_imagePageIsEnd_ $imagePageIsEnd")
                        println("f_videoPageIsEnd_ $videoPageIsEnd")
                        if (!imagePageIsEnd) imagePage += 1
                        if (!videoPageIsEnd) videoPage += 1
                        isNextPage = true
                        searchKeyword()
                    }
                }
            })
        }

        searchResultAdapter.setOnItemClickListener(object :
            SearchResultAdapter.OnItemClickListener {
            override fun onItemClick(item: SearchResultData) {
                SingletonObject.addFavoriteImage(item)
                println("search_favoriteList_ ${SingletonObject.favoriteList}")
                Utils.saveFavoriteSharedPreferences(requireContext())
            }
        })
    }

    private fun setAdapter() {
        searchResultAdapter = SearchResultAdapter()
        binding.rvSearchResult.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = searchResultAdapter
        }
    }

    private fun searchKeyword() {
        viewModel.apply {
            val keyword = binding.etSearch.text.toString()
            if (!imagePageIsEnd) searchImage(keyword, imagePage)
            if (!videoPageIsEnd) searchVideo(keyword, videoPage)
            if (imagePageIsEnd && videoPageIsEnd) isNextPage = false
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            imageMetadata.observe(viewLifecycleOwner) { imagePageIsEnd = it?.isEnd ?: true }
            videoMetadata.observe(viewLifecycleOwner) { videoPageIsEnd = it?.isEnd ?: true }
            resultList.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    println("isNextPage_ $isNextPage")
                    if (isNextPage) searchResultAdapter.addNextData(it)
                    else searchResultAdapter.addData(it)
                }
            }
        }
    }
}