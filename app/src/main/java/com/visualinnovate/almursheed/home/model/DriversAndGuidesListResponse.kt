package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class DriversAndGuidesListResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName(value = "drivers", alternate = ["guides", "driver", "guide"]) // guides
    val drivers: List<DriverAndGuideItem?>? = null,
)

data class DriverAndGuideItem(

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

    @field:SerializedName("gender")
    val gender: Any? = null,

    @field:SerializedName("admin_rating")
    val adminRating: String? = null,

    @field:SerializedName("nationality")
    val nationality: String? = null,

    @field:SerializedName("phone")
    val phone: Any? = null,

    @field:SerializedName("ratings_sum")
    val ratingsSum: Int? = null,

    @field:SerializedName("total_rating")
    val totalRating: String? = null,

    @field:SerializedName("ratings_count")
    val ratingsCount: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("status")
    val status: Any? = null,

    @field:SerializedName("price_services")
    val priceServices: List<PriceServicesItem?>? = null,
)

data class PriceServicesItem(

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("state_name")
    val stateName: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null
)