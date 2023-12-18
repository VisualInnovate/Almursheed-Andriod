package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class OfferResponse(

	@field:SerializedName("offers")
	val offers: List<OfferItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class OfferItem(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("pictures")
	val pictures: Pictures? = null,

	@field:SerializedName("status")
	val status: Int? = null
)