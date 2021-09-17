package com.bohregard.pagertest.extension

import android.util.Log

fun <T> debug(message: T) {
    val tmp = message.toString()

    if(tmp.length > 4000) {
        tmp.chunked(4000).forEach {
            Log.d("DEBUG", it)
        }
    } else {
        Log.d("DEBUG", tmp)
    }
}
