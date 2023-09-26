package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemAllDriverBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem

class AllDriverAdapter(
    private val btnDriverClickCallBack: (driver: DriverAndGuideItem) -> Unit
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
        val driverRating = itemView.driverRating

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
        Glide.with(holder.itemView.context)
            .load(driver.imageBackground)
            .into(holder.imgDriver)
        holder.driverName.text = driver.name ?: ""
        holder.city.text = driver.stateName

        // check favorite
        /*if (!driver.driverFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }*/
    }

    override fun getItemCount(): Int {
        return allDriversList?.size ?: 0
    }

    fun submitData(data: List<DriverAndGuideItem?>?) {
        allDriversList = data
        notifyDataSetChanged()
    }
}
