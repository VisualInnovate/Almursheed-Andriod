package com.visualinnovate.almursheed.driver.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.utils.Constant

class DriverAdapter(
    private val btnDriverClickCallBack: (driver: DriverAndGuideItem) -> Unit,
    private val onFavoriteClickCallBack: (driver: DriverAndGuideItem) -> Unit,
    private val onBookNowClickCallBack: (driver: DriverAndGuideItem) -> Unit,
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    private var driversList: List<DriverAndGuideItem?>? = ArrayList()

    private lateinit var binding: ItemDriverBinding

    inner class DriverViewHolder(itemView: ItemDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgCarDriver = itemView.imgCarDriver
        val imgDriver = itemView.imgUser
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

            btnBookNow.onDebouncedListener {
                onBookNowClickCallBack.invoke(driversList!![adapterPosition]!!)
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
        bindData(holder, driver!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: DriverViewHolder, driver: DriverAndGuideItem) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER || SharedPreference.getUserRole() == Constant.ROLE_GUIDES) {
            binding.btnBookNow.gone()
            binding.imgFavorite.gone()
        } else {
            binding.btnBookNow.visible()
            binding.imgFavorite.visible()
        }

        if (driver.imageBackground?.isNotEmpty() == true) {
            Glide.with(holder.itemView.context)
                .load(driver.imageBackground)
                .error(R.drawable.ic_mursheed_logo)
                .into(holder.imgCarDriver)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_mursheed_logo)
                .error(R.drawable.ic_mursheed_logo)
                .into(holder.imgCarDriver)
        }

        Glide.with(holder.itemView.context)
            .load(driver.personalPhoto)
            .error(R.drawable.ic_mursheed_logo)
            .into(holder.imgDriver)

        holder.username.text = driver.name ?: ""
        holder.city.text = driver.stateName
        holder.rating.text = (driver.totalRating ?: 0.0).toString()

        driver.priceServices?.let {
            if (it.isNotEmpty()) {
                holder.price.text = "$" + it[0]?.price.toString()
            } else {
                holder.price.text = "$0.0"
            }
        }

        // check status
        /*if (driver.status == 1) { // false -> offline
            holder.imgStatus.setImageResource(R.drawable.ic_active_online)
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_offline_active)
        }*/

        // check favorite
        if (driver.isFavourite == false) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite)
        }

        holder.imgFavorite.onDebouncedListener {
            onFavoriteClickCallBack.invoke(driver)
        }

    }

    override fun getItemCount(): Int {
        return driversList?.size ?: 0
    }

    fun submitData(drivers: List<DriverAndGuideItem?>?) {
        driversList = drivers
        notifyDataSetChanged()
    }
}
