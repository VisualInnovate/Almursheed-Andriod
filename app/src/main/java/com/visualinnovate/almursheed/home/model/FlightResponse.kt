package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class FlightResponse(

	@field:SerializedName("flights")
	val flights: List<FlightItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)


data class FlightItem(

	@field:SerializedName("country")
	val country: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: LocalizedName? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("state")
	val state: Any? = null,

	@field:SerializedName("pictures")
	val pictures: Pictures? = null
)