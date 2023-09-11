package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.home.model.DriverItem

class DriverAdapter(
    private val btnDriverClickCallBack: (driver: DriverItem) -> Unit
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    private var driversList: List<DriverItem?>? = ArrayList()

    private lateinit var binding: ItemDriverBinding

    inner class DriverViewHolder(itemView: ItemDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgDriver = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val imgStatus = itemView.imgStatus
        val username = itemView.username
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating
        private val btnBookNow = itemView.btnBookNow

        init {
            itemView.root.setOnClickListener {
                btnDriverClickCallBack.invoke(driversList!![adapterPosition]!!)
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
        val driver = driversList!![position]
        // bind view
        bindData(holder, position, driver!!)
    }

    private fun bindData(holder: DriverViewHolder, position: Int, driver: DriverItem) {
        Glide.with(holder.itemView.context)
            .load(driver.imageBackground)
            .into(holder.imgDriver)
        holder.username.text = driver.name ?: ""
        holder.city.text = driver.stateName
        // check status
        /*if (driver.status == 1) { // false -> offline
            holder.imgStatus.setImageResource(R.drawable.ic_active_online)
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_offline_active)
        }*/
        // check favorite
//        if (!driver.driverFavorite) { // false -> un favorite
//            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
//        } else {
//            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
//        }
    }

    override fun getItemCount(): Int {
        return driversList?.size ?: 0
    }

    fun submitData(drivers: List<DriverItem?>?) {
        driversList = drivers
        notifyDataSetChanged()
    }
}
