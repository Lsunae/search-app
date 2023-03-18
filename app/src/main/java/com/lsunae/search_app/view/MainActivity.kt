package com.lsunae.search_app.view

import android.os.Bundle
import com.lsunae.search_app.R
import com.lsunae.search_app.constant.MenuType
import com.lsunae.search_app.databinding.ActivityMainBinding
import com.lsunae.search_app.view.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.action_search -> {}
                    R.id.action_storage -> {}
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
}