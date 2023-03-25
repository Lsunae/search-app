package com.lsunae.search_app.view.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lsunae.search_app.R
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.FragmentSearchBinding
import com.lsunae.search_app.util.FavoriteDataManager
import com.lsunae.search_app.util.OnSingleClickListener
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.util.hideKeyboard
import com.lsunae.search_app.view.search.adapter.SearchResultAdapter
import com.lsunae.search_app.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var prevFavoriteList = arrayListOf<SearchResultData>()
    private var imagePage = 1
    private var videoPage = 1
    private var isNextPage = false
    private var imageIsEnd = false
    private var videoIsEnd = false
    private var isReset = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setAdapter()
        searchResultAdapter.searchFragment = WeakReference(this)
        setListener()
        setMetaData()
        setSearchResultList()
        setCurrentSearchKeyword()
    }

    override fun onResume() {
        super.onResume()

        Utils.loadFavoriteSharedPreferences(requireContext())
        val newFavoriteList = FavoriteDataManager.favoriteList
        if (prevFavoriteList.containsAll(newFavoriteList)) searchResultAdapter.notifyChange()
    }

    private fun setupView() {
        binding.includeActionbar.tvTitle.text = getString(R.string.search)
        setLoadingProgressBar()
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

            etSearch.setOnKeyListener { view, keyCode, keyEvent ->
                if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    resetData()
                    searchKeyword()
                    view?.hideKeyboard()
                    true
                } else {
                    false
                }
            }

            rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                    if (lastVisibleItemPosition + 1 == itemTotalCount && !isReset) {
                        isReset = false
                        val isMoreNotFound = viewModel.isMoreNotFound.value ?: false

                        if (!isMoreNotFound && !imageIsEnd || !videoIsEnd) {
                            isNextPage = true
                            if (!imageIsEnd) imagePage += 1
                            if (!videoIsEnd) videoPage += 1
                        }
                        searchKeyword()
                    }
                }
            })
        }

        searchResultAdapter.setOnFavoriteClickListener(object :
            SearchResultAdapter.OnFavoriteClickListener {
            override fun onFavoriteClick(item: SearchResultData, isChecked: Boolean) {
                if (isChecked) FavoriteDataManager.addFavoriteImage(item)
                else FavoriteDataManager.removeFavoriteImage(item)
                Utils.saveFavoriteSharedPreferences(requireContext())
            }
        })
    }

    private fun searchKeyword() {
        viewModel.apply {
            Utils.loadFavoriteSharedPreferences(requireContext())
            prevFavoriteList = FavoriteDataManager.favoriteList

            val keyword = binding.etSearch.text.toString()
            if (keyword.isEmpty() && (imagePage == 1 && videoPage == 1)) {
                isReset = true
                resetData()
                searchResultAdapter.resetItems()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.search_keyword_empty),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                isReset = false
                searchKeyword(keyword, imagePage, videoPage, imageIsEnd, videoIsEnd)
            }
            if (imageIsEnd && videoIsEnd) isNextPage = false
        }
    }

    private fun setMetaData() {
        viewModel.apply {
            imageMetadata.observe(viewLifecycleOwner) { imageIsEnd = it?.isEnd ?: true }
            videoMetadata.observe(viewLifecycleOwner) { videoIsEnd = it?.isEnd ?: true }
        }
    }

    private fun setSearchResultList() {
        viewModel.apply {
            var isMoreSearchNotFound = false
            isMoreNotFound.observe(viewLifecycleOwner) { isMoreSearchNotFound = it ?: false }
            resultList.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    binding.apply {
                        if ((isMoreSearchNotFound && imageIsEnd && videoIsEnd)) {
                            llSearchEmpty.visibility = View.GONE
                            rvSearchResult.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.search_more_result_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            llSearchEmpty.visibility = View.VISIBLE
                            rvSearchResult.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.search_result_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    binding.apply {
                        llSearchEmpty.visibility = View.GONE
                        rvSearchResult.visibility = View.VISIBLE
                    }
                    if (isNextPage) searchResultAdapter.addNextItems(it)
                    else searchResultAdapter.addItems(it)
                }
            }
        }
    }

    private fun setCurrentSearchKeyword() {
        viewModel.currentKeyword.observe(viewLifecycleOwner) {
            binding.etSearch.apply {
                if (text.toString() != it) {
                    setText(it.toString())
                    setSelection(text.length)
                }
            }
        }
    }

    private fun setLoadingProgressBar() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
    }

    private fun resetData() {
        binding.rvSearchResult.scrollToPosition(0)
        imagePage = 1
        videoPage = 1
        isNextPage = false
        imageIsEnd = false
        videoIsEnd = false
    }
}