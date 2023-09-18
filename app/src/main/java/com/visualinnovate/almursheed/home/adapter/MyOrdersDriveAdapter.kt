package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.databinding.ItemMyOrderDGHomeBinding

class MyOrdersDriveAdapter() : RecyclerView.Adapter<MyOrdersDriveAdapter.MyOrdersViewHolder>() {

    private var order: List<String> = ArrayList()

    private lateinit var binding: ItemMyOrderDGHomeBinding
    private lateinit var daysAdapter: DaysAdapter

    inner class MyOrdersViewHolder(itemView: ItemMyOrderDGHomeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
//        val dayNumber = itemView.dayNumber
//        val btnCollapse = itemView.btnCollapse
//        val routeView = itemView.routeView
//        val destination = itemView.destination
//
//        init {
//            btnCollapse.onDebouncedListener {
//                if (routeView.isVisible) {
//                    btnCollapse.setBackgroundResource(R.drawable.bg_plus_curved_green)
//                    routeView.gone()
//                } else {
//                    btnCollapse.setBackgroundResource(R.drawable.bg_minus_curved_green)
//                    routeView.visible()
//                }
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        binding = ItemMyOrderDGHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val order = order[position]
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: MyOrdersViewHolder, order: String) {
        initRecyclerView(arrayListOf("Day One" , "Day Two" , "Day Three"))
        // holder.dayNumber.text = day
    }

    override fun getItemCount(): Int {
        return order.size
    }

    fun submitData(data: List<String>) {
        order = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun initRecyclerView(selectedDays: ArrayList<String>) {
        /*daysAdapter = DaysAdapter()
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays)*/
    }
}
