package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferModel(
    var offerId: Int = 0,
    var offerImage: Int = 0, // list of images
    var offerRating: Double = 0.0,
    var offerName: String = "",
    var offerDescription: String = "",
    var offerPrice: String = "",
    var offerRealPrice: String = ""
) : Parcelable
