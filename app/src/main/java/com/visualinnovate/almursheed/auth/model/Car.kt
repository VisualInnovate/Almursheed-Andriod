package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("data")
    val carList: List<CarItem>?
)

data class CarItem(
    val id: String,
    val year: String,
    val make: String,
    val model: String
)
