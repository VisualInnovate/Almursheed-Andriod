package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.visualinnovate.almursheed.common.SharedPreference
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalizedName(

    @field:SerializedName("az")
    val az: String? = null,

    @field:SerializedName("gb")
    val gb: String? = null,

    @field:SerializedName("sa")
    val sa: String? = null
) : Parcelable {
    val localized: String
        get() {
            val languageCode = SharedPreference.getLanguage()
            return if (languageCode == "en") gb ?: "" else sa ?: ""
        }
}
