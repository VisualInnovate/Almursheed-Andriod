package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.home.model.DriverModel

class DriverAdapter(
    private val btnDriverClickCallBack: (driverModel: DriverModel) -> Unit
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    private var newsList: List<DriverModel> = ArrayList()

    private lateinit var binding: ItemDriverBinding

    inner class DriverViewHolder(itemView: ItemDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgBanner = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val imgStatus = itemView.imgStatus
        val username = itemView.username
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating
        private val btnBookNow = itemView.btnBookNow

        init {
            itemView.root.setOnClickListener {
                btnDriverClickCallBack.invoke(newsList[adapterPosition])
            }
            btnBookNow.setOnClickListener {
                // btnDriverClickCallBack.invoke(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        binding = ItemDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, position, article)
    }

    private fun bindData(holder: DriverViewHolder, position: Int, driver: DriverModel) {
        // Utils.loadImage(holder.itemView.context, driver.imageBanner, holder.imgBanner)
        holder.rating.text = driver.driverRating.toString()
        holder.username.text = driver.driverName
        holder.price.text = driver.driverPrice.toString()
        holder.city.text = driver.driverCity
        // check favorite
        if (!driver.driverFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
        // check status
        if (!driver.driverStatus) { // false -> offline
            holder.imgStatus.setImageResource(R.drawable.ic_offline_active)
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_active_online)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<DriverModel>) {
        newsList = data
        notifyDataSetChanged()
    }
}
