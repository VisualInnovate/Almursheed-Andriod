package com.visualinnovate.almursheed.commonView.myOrders.models

import com.google.gson.annotations.SerializedName

data class RateResponse(

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
