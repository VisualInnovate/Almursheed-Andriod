package com.visualinnovate.almursheed.commonView.contactUs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.invisible
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.contactUs.model.TicketItem
import com.visualinnovate.almursheed.databinding.ItemTicketBinding

class MyTicketsAdapter(
    private val onAllDetailsClickCallback: (item: TicketItem) -> Unit = {},
) : RecyclerView.Adapter<MyTicketsAdapter.ViewHolder>() {

    private var ticketList: List<TicketItem?>? = ArrayList()

    private lateinit var binding: ItemTicketBinding

    inner class ViewHolder(itemView: ItemTicketBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val status = itemView.status
        val txtAllDetails = itemView.txtAllDetails
        val ticketId = itemView.ticketId
        val priority = itemView.priority
        val subject = itemView.subject
        val type = itemView.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = ticketList!![position]!!
        // bind view
        bindData(holder, ticket)
    }

    private fun bindData(holder: ViewHolder, ticket: TicketItem?) {

        holder.ticketId.text = ticket?.id.toString()
        holder.type.text = ticket?.type
        holder.priority.text = ticket?.priority
        holder.subject.text = holder.itemView.context.getString(R.string.technical_support_issues)

        when (ticket?.status) {
            1 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorPending))
                    text = context.getString(R.string.pending)
                }
            }

            2 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorApproved))
                    text = context.getString(R.string.approved)
                }
            }

            3 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorReject))
                    text = context.getString(R.string.reject)
                }
            }

            4 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorExpire))
                    text = context.getString(R.string.expired)
                }
            }

            5 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorCancel))
                    text = context.getString(R.string.cancel)
                }
            }

            6 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.purple_700))
                    text = context.getString(R.string.paid)
                }
            }

            7 -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.purple_200))
                    text = context.getString(R.string.in_progress)
                }
            }

            else -> {
                holder.status.invisible()
            }
        }

        holder.txtAllDetails.onDebouncedListener {
            onAllDetailsClickCallback.invoke(ticket!!)
        }
    }

    override fun getItemCount(): Int {
        return ticketList?.size ?: 0
    }

    fun submitData(data: List<TicketItem?>?) {
        ticketList = data
        notifyDataSetChanged()
    }
}
