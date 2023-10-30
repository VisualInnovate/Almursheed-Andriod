package com.visualinnovate.almursheed.tourist.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemPopularLocationBinding
import com.visualinnovate.almursheed.home.model.AttractivesItem
import com.visualinnovate.almursheed.utils.Constant

class LocationAdapter(
    private val btnLocationClickCallBack: (location: AttractivesItem) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var attractiveList: List<AttractivesItem?>? = ArrayList()

    private lateinit var binding: ItemPopularLocationBinding

    inner class LocationViewHolder(itemView: ItemPopularLocationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgPopularLocation = itemView.imgPopularLocation
        val popularLocationName = itemView.popularLocationName
        val cityLocationName = itemView.cityLocationName
        val imgFavorite = itemView.imgFavorite

        init {
            itemView.root.setOnClickListener {
                btnLocationClickCallBack.invoke(attractiveList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        binding =
            ItemPopularLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val article = attractiveList!![position]!!
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: LocationViewHolder, attractive: AttractivesItem) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER) {
            binding.imgFavorite.gone()
        } else {
            binding.imgFavorite.visible()
        }

        Glide.with(holder.itemView.context)
            .load(attractive.media?.get(0)?.originalUrl)
            .into(holder.imgPopularLocation)
        holder.popularLocationName.text = attractive.name?.localized
        holder.cityLocationName.text = attractive.country?.country

        // check favorite
        /*if (!location.locationFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unFavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unFavorite)
        }*/
    }

    override fun getItemCount(): Int {
        return attractiveList?.size ?: 0
    }

    fun submitData(data: List<AttractivesItem?>?) {
        attractiveList = data
        notifyDataSetChanged()
    }
}