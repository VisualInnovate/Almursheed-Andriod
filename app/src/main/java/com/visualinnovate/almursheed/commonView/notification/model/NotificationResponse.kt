package com.visualinnovate.almursheed.commonView.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("data")
	val data: List<NotificationItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class NotificationItem(

	@field:SerializedName("notification")
	val notification: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("read_at")
	val readAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
