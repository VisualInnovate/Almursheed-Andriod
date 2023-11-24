package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AccommodationResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("accommmoditions")
	val accommodationsList: List<AccommodationItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Description(

	@field:SerializedName("gb")
	val gb: String? = null,

	@field:SerializedName("sa")
	val sa: String? = null
)

data class State(

	@field:SerializedName("is_active")
	val isActive: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("is_default")
	val isDefault: Int? = null,

	@field:SerializedName("lang")
	val lang: String? = null,

	@field:SerializedName("sort_order")
	val sortOrder: Int? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

data class Address(

	@field:SerializedName("gb")
	val gb: String? = null,

	@field:SerializedName("sa")
	val sa: String? = null
)

data class OwnerInfo(

	@field:SerializedName("gb")
	val gb: String? = null,

	@field:SerializedName("sa")
	val sa: String? = null
)

data class AccommodationItem(

	@field:SerializedName("country")
	val country: Country? = null,

	@field:SerializedName("rooms")
	val rooms: Int? = null,

	@field:SerializedName("address")
	val address: LocalizedName? = null,

	@field:SerializedName("info_status")
	val infoStatus: Int? = null,

	@field:SerializedName("category_accommodations_id")
	val categoryAccommodationsId: Int? = null,

	@field:SerializedName("owner_info")
	val ownerInfo: LocalizedName? = null,

	@field:SerializedName("description")
	val description: LocalizedName? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("pictures")
	val pictures: Pictures? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: LocalizedName? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: Int? = null,

	@field:SerializedName("state")
	val state: State? = null,

	@field:SerializedName("aval_status")
	val avalStatus: Int? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null
)

data class Country(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("is_active")
	val isActive: Int? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_default")
	val isDefault: Int? = null,

	@field:SerializedName("lang")
	val lang: String? = null,

	@field:SerializedName("sort_order")
	val sortOrder: Int? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null
)

