package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AttractivesListResponse(

    @field:SerializedName("attractives")
    val attractives: List<AttractivesItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class AttractivesItem(

    @field:SerializedName("country")
    val country: Country? = null,

    @field:SerializedName("description")
    val description: LocalizedName? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("long")
    val jsonMemberLong: String? = null,

    @field:SerializedName("pictures")
    val pictures: Pictures? = null,

    @field:SerializedName("name")
    val name: LocalizedName? = null,

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
