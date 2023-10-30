package com.visualinnovate.almursheed.commonView.myOrders.models

import com.google.gson.annotations.SerializedName

data class ChangeStatusResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

