package com.visualinnovate.almursheed.commonView.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.databinding.ItemTicketBinding

class MyTicketsAdapter(
    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {},
) : RecyclerView.Adapter<MyTicketsAdapter.ViewHolder>() {

    private var orders: List<MyOrdersItem?>? = ArrayList()

    private lateinit var binding: ItemTicketBinding

    inner class ViewHolder(itemView: ItemTicketBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val status = itemView.status
        val txtAllDetails = itemView.txtAllDetails
        val priority = itemView.priority
        val subject = itemView.subject
        val ticketId = itemView.ticketId
        val type = itemView.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders!![position]!!
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: ViewHolder, order: MyOrdersItem?) {
//        holder.country.text = order?.countryId.toString()
//        holder.dateOfEntry.text = order?.startDate
//        holder.dateOfExit.text = order?.endDate
//        holder.price.text = order?.cost + " $"

        when (order?.status) {
            "1" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorPending))
                    text = context.getString(R.string.pending)
                }
            }

            "2" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorApproved))
                    text = context.getString(R.string.approved)
                }
            }

            "3" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorReject))
                    text = context.getString(R.string.reject)
                }
            }

            "4" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorExpire))
                    text = context.getString(R.string.expired)
                }
            }

            "5" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.colorCancel))
                    text = context.getString(R.string.cancel)
                }
            }

            "6" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.purple_700))
                    text = context.getString(R.string.paid)
                }
            }

            "7" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.purple_200))
                    text = context.getString(R.string.in_progress)
                }
            }
        }

        holder.txtAllDetails.onDebouncedListener {
            onAllDetailsClickCallback.invoke(order!!)
        }
    }

    override fun getItemCount(): Int {
        return orders?.size ?: 0
    }

    fun submitData(data: ArrayList<MyOrdersItem?>?) {
        orders = data
        notifyDataSetChanged()
    }
}
