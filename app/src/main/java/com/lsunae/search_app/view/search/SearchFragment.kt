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
    private var imageIsEnd = false
    private var videoIsEnd = false

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
            incActionbar.tvTitle.text = getString(R.string.search)
        }
    }

    private fun setAdapter() {
        searchResultAdapter = SearchResultAdapter()
        binding.rvSearchResult.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = searchResultAdapter
        }
    }

    private fun setListener() {
        binding.apply {
            ivSearch.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(v: View) {
                    resetData()
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

                    if (lastVisibleItemPosition + 1 == itemTotalCount) {
                        println("f_imageIsEnd_ $imageIsEnd")
                        println("f_videoIsEnd_ $videoIsEnd")
                        if (!imageIsEnd || !videoIsEnd) {
                            isNextPage = true
                            if (!imageIsEnd) imagePage += 1
                            if (!videoIsEnd) videoPage += 1
                        }
                        searchKeyword()
                    }
                }
            })
        }

        searchResultAdapter.setOnItemClickListener(object :
            SearchResultAdapter.OnItemClickListener {
            override fun onItemClick(item: SearchResultData) {
                SingletonObject.addFavoriteImage(item)
                Utils.saveFavoriteSharedPreferences(requireContext())
            }
        })
    }

    private fun searchKeyword() {
        viewModel.apply {
            val keyword = binding.etSearch.text.toString()
            searchKeyword(keyword, imagePage, videoPage, imageIsEnd, videoIsEnd)
            if (imageIsEnd && videoIsEnd) isNextPage = false
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            imageMetadata.observe(viewLifecycleOwner) {
                println("frag_image_isEnd_ ${it?.isEnd}")
                imageIsEnd = it?.isEnd ?: true
            }
            videoMetadata.observe(viewLifecycleOwner) {
                println("frag_video_isEnd_ ${it?.isEnd}")
                videoIsEnd = it?.isEnd ?: true
            }
            resultList.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.search_result_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    println("isNextPage_ $isNextPage")
                    if (isNextPage) searchResultAdapter.addNextData(it)
                    else searchResultAdapter.addData(it)
                }
            }
        }
    }

    private fun resetData() {
        imagePage = 1
        videoPage = 1
        isNextPage = false
        imageIsEnd = false
        videoIsEnd = false
    }
}