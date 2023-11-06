package com.visualinnovate.almursheed.commonView.myOrders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.databinding.ItemMyOrderDriverGuideBinding

class MyOrderDriverAdapter(
    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onApproveClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onRejectClickCallback: (item: MyOrdersItem) -> Unit = {},
) : RecyclerView.Adapter<MyOrderDriverAdapter.ViewHolder>() {

    private var orders: List<MyOrdersItem?>? = ArrayList()

    private lateinit var binding: ItemMyOrderDriverGuideBinding

    inner class ViewHolder(itemView: ItemMyOrderDriverGuideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val country = itemView.country
        val dateOfEntry = itemView.entryDate
        val dateOfExit = itemView.exitDate
        val txtAllDetails = itemView.txtAllDetails
        val btnApprove = itemView.btnApprove
        val btnReject = itemView.btnReject
        val price = itemView.price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMyOrderDriverGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders?.get(position)
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: ViewHolder, order: MyOrdersItem?) {
        holder.country.text = order?.countryId.toString()
        holder.dateOfEntry.text = order?.startDate
        holder.dateOfExit.text = order?.endDate
        holder.price.text = order?.cost +" $"


        when (order?.status) {
            "1" -> {
                binding.btnApprove.visible()
                binding.btnReject.visible()

            }

            "2" -> {
                binding.btnApprove.gone()
                binding.btnReject.visible()
            }

            "3" -> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }

            "4" -> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }

            "5" -> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }

            "6" -> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }

            "7" -> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }
        }
        when(order?.status){
            "2" -> {
                binding.btnApprove.gone()
            }
            "6"-> {
                binding.btnApprove.gone()
                binding.btnReject.gone()
            }
            else ->{
                binding.btnApprove.visible()
                binding.btnReject.visible()
            }
        }


        holder.txtAllDetails.onDebouncedListener {
            onAllDetailsClickCallback.invoke(order!!)
        }
        holder.btnApprove.onDebouncedListener {
            onApproveClickCallback.invoke(order!!)
        }
        holder.btnReject.onDebouncedListener {
            onRejectClickCallback.invoke(order!!)
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
