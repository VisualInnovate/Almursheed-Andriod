package com.visualinnovate.almursheed.driver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemAllDriverBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.utils.Constant

class AllDriverAdapter(
    private val btnDriverClickCallBack: (driver: DriverAndGuideItem) -> Unit,
    private val onFavoriteClickCallBack: (position: Int, driver: DriverAndGuideItem) -> Unit,
) : RecyclerView.Adapter<AllDriverAdapter.AllDriverViewHolder>() {

    private var allDriversList: List<DriverAndGuideItem?>? = ArrayList()

    private lateinit var binding: ItemAllDriverBinding

    inner class AllDriverViewHolder(itemView: ItemAllDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgDriver = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val driverName = itemView.driverName
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating

        init {
            itemView.root.setOnClickListener {
                btnDriverClickCallBack.invoke(allDriversList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDriverViewHolder {
        binding = ItemAllDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllDriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllDriverViewHolder, position: Int) {
        val driver = allDriversList!![position]
        // bind view
        bindData(holder, position, driver!!)
    }

    private fun bindData(holder: AllDriverViewHolder, position: Int, driver: DriverAndGuideItem) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER
            || SharedPreference.getUserRole() == Constant.ROLE_GUIDES
        ) {
            binding.imgFavorite.gone()
        } else {
            binding.imgFavorite.visible()
        }

        Glide.with(holder.itemView.context)
            .load(driver.imageBackground)
            .into(holder.imgDriver)
        holder.driverName.text = driver.name ?: ""
        holder.city.text = driver.stateName
        holder.rating.text = (driver.totalRating ?: 0.0).toString()

        driver.priceServices?.let {
            if (it.isNotEmpty()) {
                holder.price.text = it[0]?.price.toString() +" $"
            }
        }
        // check favorite
        if (driver.isFavourite == false) { // 0 -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite)
        }

        holder.imgFavorite.onDebouncedListener {
            onFavoriteClickCallBack.invoke(position, driver)
        }
    }

    override fun getItemCount(): Int {
        return allDriversList?.size ?: 0
    }

    fun submitData(data: List<DriverAndGuideItem?>?) {
        allDriversList = data
        notifyDataSetChanged()
    }
}
