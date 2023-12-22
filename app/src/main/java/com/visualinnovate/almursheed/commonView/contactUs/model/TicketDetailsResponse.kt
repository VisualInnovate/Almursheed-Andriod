package com.visualinnovate.almursheed.commonView.contactUs.model

import com.google.gson.annotations.SerializedName

data class TicketDetailsResponse(

	@field:SerializedName("ticket")
	val ticket: TicketItem? = null,

	@field:SerializedName("status")
	val status: String? = null
)


