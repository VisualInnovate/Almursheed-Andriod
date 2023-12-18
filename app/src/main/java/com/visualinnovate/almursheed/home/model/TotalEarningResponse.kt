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
	val allProfits: Double? = null,

	@field:SerializedName("guides_profits")
	val guidesProfits: Double? = null,

	@field:SerializedName("drivers_profits")
	val driversProfits: Double? = null
)
