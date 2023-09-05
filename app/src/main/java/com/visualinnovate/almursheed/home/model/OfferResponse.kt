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

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("number")
    val number: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("title")
    val title: LocalizedName? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("pictures")
    val pictures: Pictures? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class Title(

    @field:SerializedName("gb")
    val gb: String? = null,

    @field:SerializedName("ru")
    val ru: String? = null,

    @field:SerializedName("az")
    val az: String? = null,

    @field:SerializedName("ge")
    val ge: String? = null,

    @field:SerializedName("sa")
    val sa: String? = null,

    @field:SerializedName("tr")
    val tr: String? = null
)
