package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemPopularLocationBinding
import com.visualinnovate.almursheed.home.model.LocationModel

class LocationAdapter(
    private val btnLocationClickCallBack: (locationModel: LocationModel) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var newsList: List<LocationModel> = ArrayList()

    private lateinit var binding: ItemPopularLocationBinding

    inner class LocationViewHolder(itemView: ItemPopularLocationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgPopularLocation = itemView.imgPopularLocation
        val popularLocationName = itemView.popularLocationName
        val cityLocationName = itemView.cityLocationName
        val imgFavorite = itemView.imgFavorite

        init {
            itemView.root.setOnClickListener {
                btnLocationClickCallBack.invoke(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        binding =
            ItemPopularLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: LocationViewHolder, location: LocationModel) {
        // Utils.loadImage(holder.itemView.context, location.imageBanner, holder.imgBanner)
        holder.imgPopularLocation.setImageResource(location.locationImage)
        holder.popularLocationName.text = location.locationName
        holder.cityLocationName.text = location.locationCity
        // check favorite
        if (!location.locationFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<LocationModel>) {
        newsList = data
        notifyDataSetChanged()
    }
}
