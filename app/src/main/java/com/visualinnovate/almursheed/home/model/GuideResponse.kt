package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class GuideResponse(

    @field:SerializedName("guides")
    val guides: List<GuidesItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class GuidesItem(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("gender")
    val gender: Int? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state_id")
    val stateId: Int? = null,

    // @field:SerializedName("media")
    // val media: List<Pictures?>? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    // @field:SerializedName("pictures")
    // val pictures: List<Pictures?>? = null,
    // val pictures: Pictures? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
