package com.visualinnovate.almursheed.commonView.contactUs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.commonView.contactUs.model.ConversationItem
import com.visualinnovate.almursheed.databinding.ItemTicketConversationBinding

class TicketConversationAdapter :
    RecyclerView.Adapter<TicketConversationAdapter.TicketConversationViewHolder>() {

    private var ticketConversationList: List<ConversationItem?>? = ArrayList()

    private lateinit var binding: ItemTicketConversationBinding

    inner class TicketConversationViewHolder(itemView: ItemTicketConversationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val txtMessage = itemView.txtMessage
        val message = itemView.message
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketConversationViewHolder {
        binding =
            ItemTicketConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TicketConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketConversationViewHolder, position: Int) {
        val ticket = ticketConversationList!![position]!!

        // bind view
        bindData(holder, ticket)
    }

    private fun bindData(holder: TicketConversationViewHolder, ticket: ConversationItem?) {
        if (ticket?.type == "message") {
            holder.txtMessage.text = holder.itemView.context.getString(R.string.normal_message)
        } else {
            holder.txtMessage.text = holder.itemView.context.getString(R.string.replay)
        }

        holder.message.text = ticket?.content.toString()
    }

    override fun getItemCount(): Int {
        return ticketConversationList?.size ?: 0
    }

    fun submitData(data: List<ConversationItem?>?) {
        ticketConversationList = data
        notifyDataSetChanged()
    }
}
