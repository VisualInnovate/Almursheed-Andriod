package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AttractiveDetailsResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("attractiveLocation")
	val attractiveLocation: AttractiveLocation? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class AttractiveLocation(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("name")
	val name: LocalizedName? = null,

	@field:SerializedName("description")
	val description: LocalizedName? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("photos")
	val photos: List<String?>? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("url")
	val url: Any? = null,

	@field:SerializedName("long")
	val jsonMemberLong: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null
)
