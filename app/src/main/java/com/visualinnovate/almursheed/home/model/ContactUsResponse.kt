package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class ContactUsResponse(

	@field:SerializedName("ticket")
	val ticket: Ticket? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Ticket(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
