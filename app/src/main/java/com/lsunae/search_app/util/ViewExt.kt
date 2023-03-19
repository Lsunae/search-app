package com.lsunae.search_app.util

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lsunae.search_app.R


fun ImageView.glideImageSet(image: String) {
    Glide.with(context)
        .load(image)
        .error(R.drawable.ic_error)
        .override(width, height)
        .centerCrop()
        .into(this)
}

fun View.hideKeyboard() {
    val manager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    manager?.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}