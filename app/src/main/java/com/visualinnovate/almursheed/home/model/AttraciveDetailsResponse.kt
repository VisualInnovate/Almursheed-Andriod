package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AttraciveDetailsResponse(

    @field:SerializedName("attracive")
    val attracive: Attracive? = null,

    @field:SerializedName("attractiveLocation")
    val attractiveLocation: AttractiveLocation? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Attracive(

    @field:SerializedName("country")
    val country: Country? = null,

    @field:SerializedName("description")
    val description: Description? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("long")
    val jsonMemberLong: String? = null,

    @field:SerializedName("pictures")
    val pictures: Pictures? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: Name? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state")
    val state: Any? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null
)

data class AttractiveLocation(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photos")
    val photos: List<String>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("url")
    val url: String? = null,
)

