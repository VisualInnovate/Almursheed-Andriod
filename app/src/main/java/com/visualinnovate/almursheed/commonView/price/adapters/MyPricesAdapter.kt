package com.visualinnovate.almursheed.commonView.price.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.commonView.price.models.PriceItem
import com.visualinnovate.almursheed.databinding.ItemPriceBinding

class MyPricesAdapter : RecyclerView.Adapter<MyPricesAdapter.ViewHolder>() {

    private var prices: List<PriceItem?>? = ArrayList()
    private lateinit var binding: ItemPriceBinding

    inner class ViewHolder(itemView: ItemPriceBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val cityName = itemView.cityName
        val cityPrice = itemView.cityPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = prices!![position]!!
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: ViewHolder, priceItem: PriceItem?) {
        holder.cityName.text = priceItem?.cityName.toString()
        holder.cityPrice.apply {
            text = context.getString(R.string.dollar, priceItem?.price.toString())
        }
    }

    override fun getItemCount(): Int {
        return prices?.size ?: 0
    }

    fun submitData(data: ArrayList<PriceItem?>?) {
        prices = data
        notifyDataSetChanged()
    }

    fun getPricesList(): List<PriceItem?> {
        return prices!!
    }
}
