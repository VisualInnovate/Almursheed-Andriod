package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(

	@field:SerializedName("is_favourite")
	val isFavourite: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
