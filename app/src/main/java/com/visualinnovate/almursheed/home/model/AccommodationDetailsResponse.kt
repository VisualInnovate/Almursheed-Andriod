package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AccommodationDetailsResponse(

    @field:SerializedName("accommmodition")
    val accommodation: AccommodationItem? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

