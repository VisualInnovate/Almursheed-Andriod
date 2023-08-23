package com.visualinnovate.almursheed.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

object Utils {

    fun loadImage(context: Context, urlToImage: Int, imgView: ImageView) {
        Glide.with(context)
            .load(urlToImage)
            .into(imgView)
    }

    fun loadGifImage(context: Context, urlToImage: Int, imgView: ImageView) {
        Glide.with(context)
            .asGif()
            .load(urlToImage)
            .into(imgView)
    }
}
