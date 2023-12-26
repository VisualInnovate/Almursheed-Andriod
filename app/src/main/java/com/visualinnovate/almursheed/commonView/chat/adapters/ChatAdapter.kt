package com.visualinnovate.almursheed.commonView.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.commonView.chat.model.Message
import com.visualinnovate.almursheed.databinding.ItemMessageReceiveBinding
import com.visualinnovate.almursheed.databinding.ItemMessageSendBinding

class ChatAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messages: ArrayList<Message> = ArrayList()
    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val binding = ItemMessageSendBinding.inflate(inflater, parent, false)
                SentMessageViewHolder(binding)
            }
            VIEW_TYPE_RECEIVED -> {
                val binding = ItemMessageReceiveBinding.inflate(inflater, parent, false)
                ReceivedMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when (holder.itemViewType) {
            VIEW_TYPE_SENT -> {
                val sentViewHolder = holder as SentMessageViewHolder
                sentViewHolder.binding.message.text = message.content
            }
            VIEW_TYPE_RECEIVED -> {
                val receivedViewHolder = holder as ReceivedMessageViewHolder
                receivedViewHolder.binding.message.text = message.content
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].type == "messages" ||messages[position].type == "message" ) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    fun submitList(messagesList: ArrayList<Message>) {
        messages = messagesList
        notifyDataSetChanged()
    }

    // ViewHolders
    inner class SentMessageViewHolder(val binding: ItemMessageSendBinding) :
        RecyclerView.ViewHolder(binding.root)

   inner class ReceivedMessageViewHolder(val binding: ItemMessageReceiveBinding) :
        RecyclerView.ViewHolder(binding.root)
}
