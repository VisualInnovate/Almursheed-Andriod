package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class City(

    @field:SerializedName("states")
    val states: List<CityItem>?
)

data class CityItem(

    @field:SerializedName("country_id")
    var countryId: String,

    @field:SerializedName("state_id")
    val stateId: String,

    @field:SerializedName("state")
    val state: String
)
