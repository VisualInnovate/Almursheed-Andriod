package com.visualinnovate.almursheed.home.model

data class OfferModel(
    var offerId: Int = 0,
    var offerImage: Int = 0,
    var offerRating: Double = 0.0,
    var offerName: String = "",
    var offerFavorite: Boolean = false // false -> unFavorite, true -> favorite
)
