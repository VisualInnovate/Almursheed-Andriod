package com.visualinnovate.almursheed.commonView.contactUs.model

import com.google.gson.annotations.SerializedName

data class MyTicketResponse(

	@field:SerializedName("ticket_user_id")
	val ticketUserId: Int? = null,

	@field:SerializedName("tickets")
	val tickets: List<TicketItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ConversationItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("ticket_id")
	val ticketId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class TicketItem(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("conversation")
	val conversation: List<ConversationItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("ticket_user_id")
	val ticketUserId: Int? = null,

	@field:SerializedName("user")
	val user: User? = null,
)

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

