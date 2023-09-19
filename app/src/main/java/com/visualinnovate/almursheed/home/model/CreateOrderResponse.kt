package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("order")
    val order: OrderItem? = null
)

data class OrderItem(

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("cost")
    val cost: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("trip_type")
    val tripType: Int? = null,

    @field:SerializedName("tourist_id")
    val touristId: Int? = null,

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
