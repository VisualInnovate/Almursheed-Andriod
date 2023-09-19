package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.databinding.ItemDriverGuideBinding
import com.visualinnovate.almursheed.home.model.DriverItem

class SelectDriverOrGuideAdapter(
    private val btnDriverClickCallBack: (driver: DriverItem) -> Unit,
) : RecyclerView.Adapter<SelectDriverOrGuideAdapter.SelectDriverOrGuideViewHolder>() {

    private var driversList: List<DriverItem?>? = ArrayList()

    private lateinit var binding: ItemDriverGuideBinding

    inner class SelectDriverOrGuideViewHolder(itemView: ItemDriverGuideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgDriver = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val username = itemView.username
        val rating = itemView.rating
        val root = itemView.root

        init {
            itemView.root.setOnClickListener {
                btnDriverClickCallBack.invoke(driversList!![adapterPosition]!!)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDriverOrGuideViewHolder {
        binding = ItemDriverGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectDriverOrGuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectDriverOrGuideViewHolder, position: Int) {
        val driver = driversList!![position]
        // bind view
        bindData(holder, position, driver!!)
    }

    private fun bindData(holder: SelectDriverOrGuideViewHolder, position: Int, driver: DriverItem) {
        holder.imgFavorite.gone()

      //  holder.rating.text =
        Glide.with(holder.itemView.context)
            .load(driver.imageBackground)
            .into(holder.imgDriver)
        holder.username.text = driver.name ?: ""
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
