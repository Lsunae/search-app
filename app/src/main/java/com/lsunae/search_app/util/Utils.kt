package com.lsunae.search_app.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun dateFormat(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return dateFormat.format(date)
    }
}