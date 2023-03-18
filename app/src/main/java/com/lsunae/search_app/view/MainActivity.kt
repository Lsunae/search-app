package com.lsunae.search_app.view

import android.os.Bundle
import com.lsunae.search_app.R
import com.lsunae.search_app.constant.MenuType
import com.lsunae.search_app.databinding.ActivityMainBinding
import com.lsunae.search_app.view.base.BaseActivity
import com.lsunae.search_app.view.search.SearchFragment
import com.lsunae.search_app.view.storage.StorageFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var searchFragment: SearchFragment? = null
    private var storageFragment: StorageFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.action_search -> changeFragment(MenuType.SEARCH)
                    R.id.action_storage -> changeFragment(MenuType.STORAGE)
                }
                true
            }
            selectMenuTabIndex(MenuType.SEARCH)
        }
    }

    private fun selectMenuTabIndex(type: MenuType?) {
        val navigation = binding.bottomNavigationView
        runOnUiThread {
            when (type) {
                MenuType.STORAGE -> navigation.selectedItemId = R.id.action_storage
                else -> navigation.selectedItemId = R.id.action_search
            }
        }
    }

    private fun changeFragment(type: MenuType) {
        when (type) {
            MenuType.SEARCH -> {
                if (searchFragment == null) {
                    searchFragment = SearchFragment()
                    supportFragmentManager.beginTransaction()
                        .add(binding.mainContent.id, searchFragment!!).commit()
                } else {
                    supportFragmentManager.beginTransaction().show(searchFragment!!).commit()
                    searchFragment!!.onResume()
                }
                if (storageFragment != null) supportFragmentManager.beginTransaction()
                    .hide(storageFragment!!).commit()
            }
            MenuType.STORAGE -> {
                if (storageFragment == null) {
                    storageFragment = StorageFragment()
                    supportFragmentManager.beginTransaction()
                        .add(binding.mainContent.id, storageFragment!!).commit()
                } else {
                    supportFragmentManager.beginTransaction().show(storageFragment!!)
                        .commit()
                    storageFragment!!.onResume()
                }
                if (searchFragment != null) supportFragmentManager.beginTransaction()
                    .hide(searchFragment!!).commit()
            }
        }
    }
}