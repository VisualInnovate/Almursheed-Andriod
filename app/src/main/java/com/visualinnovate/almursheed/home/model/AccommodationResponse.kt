package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AccommodationResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("accommmoditions")
    val accommodations: List<AccommodationItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Photo(

    @field:SerializedName("photos")
    val photos: List<PhotosItem?>? = null
)

data class Address(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class Description(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class AccommodationItem(

    @field:SerializedName("country")
    val country: Country? = null,

    @field:SerializedName("address")
    val address: LocalizedName? = null,

    @field:SerializedName("info_status")
    val infoStatus: Int? = null,

    @field:SerializedName("owner_info")
    val ownerInfo: LocalizedName? = null,

    @field:SerializedName("description")
    val description: LocalizedName? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    // @field:SerializedName("pictures")
    // val pictures: Pictures? = null,

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

data class OwnerInfo(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class Name(

    @field:SerializedName("gb")
    val gb: String? = null
)
