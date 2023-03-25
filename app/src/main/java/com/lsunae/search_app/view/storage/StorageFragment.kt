package com.lsunae.search_app.view.storage

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.lsunae.search_app.R
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.FragmentStorageBinding
import com.lsunae.search_app.util.FavoriteDataManager
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.view.base.BaseFragment
import com.lsunae.search_app.view.storage.adapter.StorageAdapter
import java.lang.ref.WeakReference


class StorageFragment : BaseFragment<FragmentStorageBinding>(R.layout.fragment_storage) {
    private lateinit var storageAdapter: StorageAdapter
    private var isFirst = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setAdapter()
        storageAdapter.storageFragment = WeakReference(this)
        setListener()
    }

    override fun onResume() {
        super.onResume()

        loadStorage()
    }

    private fun setupView() {
        binding.apply {
            incActionbar.tvTitle.text = getString(R.string.storage)
        }
    }

    private fun setListener() {
        storageAdapter.setOnFavoriteClickListener(object : StorageAdapter.OnFavoriteClickListener {
            override fun onFavoriteClick(
                item: SearchResultData,
                position: Int,
                isChecked: Boolean
            ) {
                if (isChecked) FavoriteDataManager.addFavoriteImage(item)
                else {
                    FavoriteDataManager.removeFavoriteImage(position)
                    storageAdapter.notifyRemovedItem(position)
                }
                Utils.saveFavoriteSharedPreferences(requireContext())
                storageAdapter.notifyChange()
            }
        })
    }

    private fun loadStorage() {
        if (isFirst) {
            isFirst = false
            Utils.loadFavoriteSharedPreferences(requireContext())
        }
        storageAdapter.addItems(FavoriteDataManager.favoriteList)
    }

    private fun setAdapter() {
        storageAdapter = StorageAdapter()
        binding.rvStorage.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = storageAdapter
        }
    }
}