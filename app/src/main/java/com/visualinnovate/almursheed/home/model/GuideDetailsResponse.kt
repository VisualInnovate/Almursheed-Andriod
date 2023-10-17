package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class GuideDetailsResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val guide: DriverAndGuideItem? = null
)

