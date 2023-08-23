package com.visualinnovate.almursheed.home.model

data class LocationModel(
    var locationId: Int = 0,
    var locationImage: Int = 0,
    var locationName: String = "",
    var locationCity: String = "",
    var locationFavorite: Boolean = false
)
