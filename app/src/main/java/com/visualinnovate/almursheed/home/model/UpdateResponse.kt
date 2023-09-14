package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName
import com.visualinnovate.almursheed.auth.model.User

data class UpdateResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("user")
    val user: List<User?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)