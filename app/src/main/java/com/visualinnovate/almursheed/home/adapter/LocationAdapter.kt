package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemPopularLocationBinding
import com.visualinnovate.almursheed.home.model.AttractivesItem

class LocationAdapter(
    private val btnLocationClickCallBack: (location: AttractivesItem) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var attractivesList: List<AttractivesItem?>? = ArrayList()

    private lateinit var binding: ItemPopularLocationBinding

    inner class LocationViewHolder(itemView: ItemPopularLocationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgPopularLocation = itemView.imgPopularLocation
        val popularLocationName = itemView.popularLocationName
        val cityLocationName = itemView.cityLocationName
        val imgFavorite = itemView.imgFavorite

        init {
            itemView.root.setOnClickListener {
                btnLocationClickCallBack.invoke(attractivesList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        binding =
            ItemPopularLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val article = attractivesList!![position]!!
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: LocationViewHolder, attractives: AttractivesItem) {
        Glide.with(holder.itemView.context)
            .load(attractives.media?.get(0)?.originalUrl)
            .into(holder.imgPopularLocation)
        holder.popularLocationName.text = attractives.name?.localized
        holder.cityLocationName.text = attractives.country?.country
        // check favorite
        /*if (!location.locationFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }*/
    }

    override fun getItemCount(): Int {
        return attractivesList?.size ?: 0
    }

    fun submitData(data: List<AttractivesItem?>?) {
        attractivesList = data
        notifyDataSetChanged()
    }
}
