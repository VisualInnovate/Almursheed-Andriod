package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemSeeAllLocationBinding
import com.visualinnovate.almursheed.home.model.LocationModel

class SeeAllLocationAdapter(
    private val btnLocationClickCallBack: (location: LocationModel) -> Unit
) : RecyclerView.Adapter<SeeAllLocationAdapter.SeeAllLocationViewHolder>() {

    private var newsList: List<LocationModel> = ArrayList()

    private lateinit var binding: ItemSeeAllLocationBinding

    inner class SeeAllLocationViewHolder(itemView: ItemSeeAllLocationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imglocation = itemView.imgLocation
        val imgFavorite = itemView.imgFavorite
        val locationName = itemView.locationName
        val countryAndCity = itemView.countryAndCity
        private val imgGoogleMap = itemView.imgGoogleMap

        init {
            itemView.root.setOnClickListener {
                btnLocationClickCallBack.invoke(newsList[adapterPosition])
            }
            imgGoogleMap.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeAllLocationViewHolder {
        binding =
            ItemSeeAllLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeeAllLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeeAllLocationViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: SeeAllLocationViewHolder, location: LocationModel) {
        // set data
        // Utils.loadImage(holder.itemView.context, location.imageBanner, holder.imgBanner)
        // holder.imglocation.background.setImageResource(location.locationImage)
        holder.locationName.text = location.locationName
        holder.countryAndCity.text = location.locationCity
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
