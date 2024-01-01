package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(

    @field:SerializedName("cost")
    val cost: Double? = null,

    @field:SerializedName("sub_total")
    val subTotal: Double? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("country_price")
    val countryPrice: CountryPrice? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("order_id")
    val orderId: Int? = null
)

data class CountryPrice(

    @field:SerializedName("fees")
    val fees: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Any? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Any? = null,

    @field:SerializedName("tax")
    val tax: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
