package com.visualinnovate.almursheed.commonView.myOrders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.databinding.ItemMyOrderDGHomeBinding
import com.visualinnovate.almursheed.home.adapter.DaysAdapter

class MyOrderDriverAdapter() : RecyclerView.Adapter<MyOrderDriverAdapter.ViewHolder>() {

    private var orders: List<MyOrdersItem?>? = ArrayList()
    private var days: ArrayList<DayModel> = ArrayList()
    private lateinit var daysAdapter: DaysAdapter

    private lateinit var binding: ItemMyOrderDGHomeBinding

    inner class ViewHolder(itemView: ItemMyOrderDGHomeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val country = itemView.country
        val dateOfEntry = itemView.entryDate
        val dateOfExit = itemView.exitDate
        val status = itemView.status
        val days = itemView.daysRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMyOrderDGHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders!![position]!!
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: ViewHolder, order: MyOrdersItem?) {
        if (days.isEmpty()) {
            getDaysList(order)
        }
        initRecyclerView(holder, days)
        holder.country.text = order?.countryId.toString()
        holder.dateOfEntry.text = order?.startDate
        holder.dateOfExit.text = order?.endDate
        when (order?.status) {
            "1" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.purple_700))
                    text = "Active"
                } }
            "2" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.orange))
                    text = "Pending"
                }
            }
            "3" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.light_green))
                    text = "Approve"
                }
            }
            "4" -> {
                holder.status.apply {
                    setBackgroundColor(resources.getColor(R.color.red))
                    text = "Cancel"
                }
            }
        }
    }

    private fun getDaysList(order: MyOrdersItem?) {
        order?.orderDetails?.forEach { orderDetails ->
            orderDetails.let {
                val day = DayModel(it!!.state.toString(), it.date.toString())
                days.add(day)
            }
        }
    }

    private fun initRecyclerView(holder: ViewHolder, selectedDays: ArrayList<DayModel>) {
        daysAdapter = DaysAdapter(null)
        holder.days.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays)
    }
    override fun getItemCount(): Int {
        return orders?.size ?: 0
    }

    fun submitData(data: ArrayList<MyOrdersItem?>?) {
        orders = data
        notifyDataSetChanged()
    }
}
