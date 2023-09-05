package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class DriverLatestResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("drivers")
    val drivers: List<DriversItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
