package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class DriverDetailsResponse(

    @field:SerializedName("driver")
    val driver: Driver? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Driver(

    @field:SerializedName("gender")
    val gender: Int? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("media")
    val media: List<Any?>? = null,

    @field:SerializedName("pictures")
    val pictures: Any? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("driver_licence_number")
    val driverLicenceNumber: String? = null,

    @field:SerializedName("car_number")
    val carNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("gov_id")
    val govId: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state_id")
    val stateId: Int? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
