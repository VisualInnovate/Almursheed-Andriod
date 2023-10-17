package com.visualinnovate.almursheed.commonView.myOrders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.models.OrderDetailsItem
import com.visualinnovate.almursheed.databinding.ItemMyOrderDGHomeBinding
import com.visualinnovate.almursheed.home.adapter.DaysAdapter

class MyOrdersTouristAdapter(
    private val onAddRateClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onPaidClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onCancelClickCallback: (item: MyOrdersItem) -> Unit = {},
) : RecyclerView.Adapter<MyOrdersTouristAdapter.ViewHolder>() {

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
        // val days = itemView.daysRecyclerView
        val btnAddRate = itemView.btnAddRate
        val txtAllDetails = itemView.txtAllDetails
        val btnPaid = itemView.btnPaid
        val btnCancel = itemView.btnCancel
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
        // init recycler days
        // initRecyclerView(holder, days, order?.orderDetails)

        holder.country.text = order?.countryId.toString()
        holder.dateOfEntry.text = order?.startDate
        holder.dateOfExit.text = order?.endDate
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
                    text = context.getString(R.string.notcompleted)
                }
            }
        }

        holder.btnAddRate.onDebouncedListener {
            onAddRateClickCallback.invoke(order!!)
        }

        holder.txtAllDetails.onDebouncedListener {
            onAllDetailsClickCallback.invoke(order!!)
        }

        holder.btnPaid.onDebouncedListener {
            onPaidClickCallback.invoke(order!!)
        }

        holder.btnCancel.onDebouncedListener {
            onCancelClickCallback.invoke(order!!)
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

    /*private fun initRecyclerView(
        holder: ViewHolder,
        selectedDays: ArrayList<DayModel>,
        orderDetails: List<OrderDetailsItem?>?
    ) {
        daysAdapter = DaysAdapter(null, false)
        holder.days.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays, orderDetails)
    }*/

    override fun getItemCount(): Int {
        return orders?.size ?: 0
    }

    fun submitData(data: ArrayList<MyOrdersItem?>?) {
        orders = data
        notifyDataSetChanged()
    }
}
