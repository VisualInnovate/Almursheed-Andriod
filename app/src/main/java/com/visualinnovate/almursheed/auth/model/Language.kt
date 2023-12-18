package com.visualinnovate.almursheed.auth.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Language(
    @SerializedName("languages")
    val languageList: List<LanguageItem>?
): Parcelable

@Parcelize
data class LanguageItem(
    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("lang")
    var lang: String = "",

    @SerializedName("iso_code")
    val isoCode: String = "",

    @SerializedName("native")
    val native: String = "",

    @SerializedName("is_default")
    val isDefault: Int = -1,

    @SerializedName("is_rtl")
    val isRtl: Int = -1,

    @SerializedName("is_active")
    val isActive: Int = -1,
) : Parcelable
