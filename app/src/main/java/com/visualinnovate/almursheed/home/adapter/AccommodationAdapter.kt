package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAccommodationBinding
import com.visualinnovate.almursheed.home.model.AccommodationModel

class AccommodationAdapter(
    private val btnAccommodationClickCallBack: (accommodation: AccommodationModel) -> Unit
) : RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder>() {

    private var newsList: List<AccommodationModel> = ArrayList()

    private lateinit var binding: ItemAccommodationBinding

    inner class AccommodationViewHolder(itemView: ItemAccommodationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgAccommodation = itemView.imgAccommodation
        val imgFavorite = itemView.imgFavorite
        val accommodationName = itemView.accommodationName
        val countryAndCity = itemView.countryAndCity
        private val imgGoogleMap = itemView.imgGoogleMap

        init {
            itemView.root.setOnClickListener {
                btnAccommodationClickCallBack.invoke(newsList[adapterPosition])
            }
            imgGoogleMap.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccommodationViewHolder {
        binding =
            ItemAccommodationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccommodationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccommodationViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: AccommodationViewHolder, accommodation: AccommodationModel) {
        // set data
        // Utils.loadImage(holder.itemView.context, accommodation.imageBanner, holder.imgBanner)
        holder.imgAccommodation.setImageResource(accommodation.accommodationImage)
        holder.accommodationName.text = accommodation.accommodationName
        holder.countryAndCity.text = accommodation.accommodationLocation
        // check favorite
        if (!accommodation.accommodationFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<AccommodationModel>) {
        newsList = data
        notifyDataSetChanged()
    }
}
