package com.visualinnovate.almursheed.commonView.more.aboutUs.model

import com.google.gson.annotations.SerializedName
import com.visualinnovate.almursheed.home.model.LocalizedName

data class AboutUsResponse(

	@field:SerializedName("pages")
	val pages: List<PagesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class PagesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("description")
	val description: LocalizedName? = null,

	@field:SerializedName("title")
	val title: LocalizedName? = null
)
