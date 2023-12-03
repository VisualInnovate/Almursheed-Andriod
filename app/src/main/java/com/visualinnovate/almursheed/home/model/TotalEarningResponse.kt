package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class TotalEarningResponse(

	@field:SerializedName("profits")
	val profits: Profits? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profits(

	@field:SerializedName("all_profits")
	val allProfits: Int? = null,

	@field:SerializedName("guides_profits")
	val guidesProfits: Int? = null,

	@field:SerializedName("drivers_profits")
	val driversProfits: Int? = null
)
