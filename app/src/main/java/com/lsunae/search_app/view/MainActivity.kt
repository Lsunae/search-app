package com.lsunae.search_app.view

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.lsunae.search_app.R
import com.lsunae.search_app.databinding.ActivityMainBinding
import com.lsunae.search_app.util.MenuType
import com.lsunae.search_app.view.search.SearchFragment
import com.lsunae.search_app.view.storage.StorageFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var searchFragment: SearchFragment? = null
    private var storageFragment: StorageFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
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