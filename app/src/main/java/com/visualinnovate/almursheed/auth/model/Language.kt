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
    val id: Int = -1,
    var lang: String = "",
    val isoCode: String = "",
    val isDefault: Int = -1,
    val isRtl: Int = -1,
    val isActive: Int = -1,
    val jsonMemberNative: String = ""
) : Parcelable
