package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class DriverListResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName(value = "drivers", alternate = ["guides", "driver", "guide"])
    val drivers: List<DriverItem?>? = null,
)

data class DriverItem(

    @field:SerializedName("state_name")
    val stateName: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state_id")
    val stateId: Int? = null,

    @field:SerializedName("image_background")
    val imageBackground: String? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("car_model")
    val carModel: String? = null,

    @field:SerializedName("car_photo")
    val carPhoto: List<String?>? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("car_date")
    val carDate: String? = null,

    @field:SerializedName("lang")
    val lang: List<Any?>? = null,

    @field:SerializedName("car_type")
    val carType: String? = null,

    @field:SerializedName("personal_photo")
    val personalPhoto: String? = null,

)
