package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class UpdateDriverResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: List<UserItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UserItem(

	@field:SerializedName("car_brand_name")
	val carBrandName: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("car_manufacturing_date")
	val carManufacturingDate: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("car_type")
	val carType: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("driver_licence_number")
	val driverLicenceNumber: String? = null,

	@field:SerializedName("car_number")
	val carNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("gov_id")
	val govId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("status")
	val status: Any? = null
)
