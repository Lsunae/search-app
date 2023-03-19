package com.lsunae.search_app.util

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