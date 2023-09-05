package com.visualinnovate.almursheed.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

object Utils {

    val nationalities = HashMap<String, String>()
    val countries = HashMap<String, String>()
    val cities = HashMap<String, Int>()
    val carYears = HashMap<String, String>()
    val carBrand = HashMap<String, String>()
    val carType = HashMap<String, String>()
    val languages = HashMap<String, Int>()

    fun loadImage(context: Context, urlToImage: Int, imgView: ImageView) {
        Glide.with(context)
            .load(urlToImage)
            .into(imgView)
    }
}
