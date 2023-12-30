package com.visualinnovate.almursheed.commonView.chat.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(

    @field:SerializedName("conversation_id")
    val conversationId: Int? = null,

    @field:SerializedName("conversation")
    val conversation: ArrayList<Message>? = null,

    @field:SerializedName("message")
    val message: Message? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("error")
    val errorMessage: String? = null,
)

data class Message(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("content")
    val content: String? = null,
)
