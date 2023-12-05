package com.visualinnovate.almursheed.auth.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageResponse(

    @field:SerializedName("languagesable_id")
    val languagesableId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("language")
    val language: Languagee? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("languagesable_type")
    val languagesableType: String? = null,

    @field:SerializedName("language_id")
    val languageId: Int? = null,
) : Parcelable

@Parcelize
data class Languagee(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lang")
    val lang: String? = null,
) : Parcelable
