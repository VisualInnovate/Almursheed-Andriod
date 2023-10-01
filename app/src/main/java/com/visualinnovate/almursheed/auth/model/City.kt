package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class City(

    @field:SerializedName("states")
    val states: List<StatesItem>?
)

data class StatesItem(

    @field:SerializedName("country_id")
    var countryId: Int,

    @field:SerializedName("state_id")
    val stateId: Int,

    @field:SerializedName("state")
    val state: String
)
