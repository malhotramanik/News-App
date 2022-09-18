package com.example.newsapp.utils

import android.util.Log
import android.view.View

private const val LOG_TAG = "NEWS_APP_LOG"
fun log(msg:String){
    Log.d(LOG_TAG,msg)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}
