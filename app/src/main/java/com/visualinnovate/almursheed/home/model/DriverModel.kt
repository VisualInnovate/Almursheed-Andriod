package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DriverModel(
    var driverId: Int = 0,
    var driverImage: Int = 0,
    var driverRating: Double = 0.0,
    var driverName: String = "",
    var driverStatus: Boolean = false, // false -> offline, true -> online
    var driverPrice: Double = 0.0,
    var driverCity: String = "",
    var driverFavorite: Boolean = false // false -> unFavorite, true -> favorite
) : Parcelable
