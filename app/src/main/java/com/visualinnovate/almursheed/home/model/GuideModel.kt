package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuideModel(
    var guideId: Int = 0,
    var guideImage: Int = 0,
    var guideRating: Double = 0.0,
    var guideName: String = "",
    var guidePrice: Double = 0.0,
    var guideCity: String = "",
    var guideFavorite: Boolean = false,
    var language: String = "",
    var guideCountry: String = "",
    var guideDescription: String = ""
) : Parcelable
