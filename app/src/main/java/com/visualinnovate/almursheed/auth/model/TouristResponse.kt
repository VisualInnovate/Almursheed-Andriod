package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class TouristResponse(

	@field:SerializedName("tourist")
	val tourist: Tourist? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Tourist(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("state_id")
	val stateId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("country_id")
	val countryId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
