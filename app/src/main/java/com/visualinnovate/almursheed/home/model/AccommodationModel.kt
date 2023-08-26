package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccommodationModel(
    var accommodationId: Int = 0,
    // list of Images
    var accommodationImage: Int = 0,
    var accommodationName: String = "",
    var accommodationLocation: String = "",
    var accommodationFavorite: Boolean = false,
    var accommodationGoogleMap: String = "",
    var accommodationPin: String = "",
    var accommodationDescription: String = ""
) : Parcelable
