package com.lsunae.search_app.view.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lsunae.search_app.R
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.FragmentStorageBinding
import com.lsunae.search_app.util.FavoriteDataManager
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.view.storage.adapter.StorageAdapter
import java.lang.ref.WeakReference


class StorageFragment : Fragment() {
    private lateinit var binding: FragmentStorageBinding
    private lateinit var storageAdapter: StorageAdapter
    private var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageBinding.inflate(inflater, container, false)

        return binding.root
    }

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
            includeActionbar.tvTitle.text = getString(R.string.storage)
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