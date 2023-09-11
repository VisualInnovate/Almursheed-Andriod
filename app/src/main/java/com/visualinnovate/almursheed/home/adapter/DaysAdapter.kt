package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemDaysBinding

class DaysAdapter() : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    private var days: List<String> = ArrayList()

    private lateinit var binding: ItemDaysBinding

    inner class DaysViewHolder(itemView: ItemDaysBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val dayNumber = itemView.dayNumber
        val btnCollapse = itemView.btnCollapse
        val routeView = itemView.routeView
        val whereTo = itemView.edtWhereFrom
        val whereFrom = itemView.edtWhereFrom

        init {
            btnCollapse.onDebouncedListener {
                if (routeView.isVisible) {
                    btnCollapse.setBackgroundResource(R.drawable.bg_plus_curved_green)
                    routeView.gone()
                } else {
                    btnCollapse.setBackgroundResource(R.drawable.bg_minus_curved_green)
                    routeView.visible()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        binding = ItemDaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val day = days[position]
        // bind view
        bindData(holder, day)
    }

    private fun bindData(holder: DaysViewHolder, day: String) {
        holder.dayNumber.text = day
    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun submitData(data: List<String>) {
        days = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
