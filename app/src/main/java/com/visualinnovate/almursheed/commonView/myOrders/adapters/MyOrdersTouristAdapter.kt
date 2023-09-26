package com.visualinnovate.almursheed.commonView.myOrders.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.databinding.ItemMyOrderTouristBinding

class MyOrdersTouristAdapter(
    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onApproveClickCallback: (item: MyOrdersItem) -> Unit = {},
    private val onRejectClickCallback: (item: MyOrdersItem) -> Unit = {},

) : RecyclerView.Adapter<MyOrdersTouristAdapter.ViewHolder>() {

    private var orders: List<MyOrdersItem?>? = ArrayList()

    private lateinit var binding: ItemMyOrderTouristBinding

    inner class ViewHolder(itemView: ItemMyOrderTouristBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val country = itemView.country
        val dateOfEntry = itemView.entryDate
        val dateOfExit = itemView.exitDate
        val txtAllDetails = itemView.txtAllDetails
        val btnApprove = itemView.btnApprove
        val btnReject = itemView.btnReject
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMyOrderTouristBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
