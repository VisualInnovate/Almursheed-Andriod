package com.visualinnovate.almursheed.commonView.myOrders.models

import com.google.gson.annotations.SerializedName

data class MyOrdersModel(

    @field:SerializedName("myOrders")
    val myOrders: ArrayList<MyOrdersItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,
)

data class MyOrdersItem(

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("tourist_name")
    val touristName: String? = null,

    @field:SerializedName("cost")
    val cost: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("tourist_id")
    val touristId: Int? = null,

    @field:SerializedName("trip_type")
    val tripType: String? = null,

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("vendor")
    val vendor: String? = null,

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
    val status: String? = null,
	
    @field:SerializedName("rating")
    val rate: String? = null,

    @field:SerializedName("order_details")
    val orderDetails: List<OrderDetailsItem?>? = null,
)

data class OrderDetailsItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("routing")
    val routing: String? = null,

    @field:SerializedName("price_city")
    val priceCity: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("order_id")
    val orderId: Int? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null,
)
