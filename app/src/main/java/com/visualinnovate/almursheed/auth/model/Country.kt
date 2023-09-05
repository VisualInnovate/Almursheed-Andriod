package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class CountryItem(
    val id: String,
    val country_id: String,
    val country: String,
    val nationality: String,
    val is_default: String,
    val is_active: String,
    val sort_order: String,
    val lang: String,
)

data class Country(
    @SerializedName("data")
    val countryList: List<CountryItem>?
)
