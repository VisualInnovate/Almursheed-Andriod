package com.visualinnovate.almursheed.commonView.price.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PricesResponse(

    @field:SerializedName("priceServices")
    val priceServices: ArrayList<PriceItem?>? = null,
)

@Parcelize
data class PriceItem(

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("state_name")
    val cityName: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null,
): Parcelable
