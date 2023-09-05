package com.visualinnovate.almursheed.auth.model

import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("languages")
    val languageList: List<LanguageItem>?
)

data class LanguageItem(
    val id: Int,
    val lang: String,
    val isoCode: String,
    val isDefault: Int,
    val isRtl: Int,
    val isActive: Int,
    val jsonMemberNative: String
)
