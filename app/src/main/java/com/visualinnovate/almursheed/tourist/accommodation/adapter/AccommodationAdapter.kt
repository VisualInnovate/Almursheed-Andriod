package com.visualinnovate.almursheed.tourist.accommodation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAccommodationBinding
import com.visualinnovate.almursheed.home.model.AccommodationItem

class AccommodationAdapter(
    private val btnAccommodationClickCallBack: (accommodation: AccommodationItem) -> Unit
) : RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder>() {

    private var accommodationList: List<AccommodationItem?>? = ArrayList()

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
                btnAccommodationClickCallBack.invoke(accommodationList!![adapterPosition]!!)
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
        val accommodation = accommodationList!![position]!!
        // bind view
        bindData(holder, position, accommodation)
    }

    private fun bindData(
        holder: AccommodationViewHolder,
        position: Int,
        accommodation: AccommodationItem
    ) {
        // set data
        Glide.with(holder.itemView.context)
            // .load(accommodation.media?.forEach { it?.originalUrl })
            .load(R.drawable.img_test)
            .into(holder.imgAccommodation)
        holder.accommodationName.text = accommodation.name?.localized ?: ""
        holder.countryAndCity.text = accommodation.address?.localized ?: ""
//        // check favorite
//        if (!accommodation.isFavorite) { // false -> un favorite
//            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
//        } else {
//            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
//        }
    }

    override fun getItemCount(): Int {
        return accommodationList?.size ?: 0
    }

    fun submitData(data: List<AccommodationItem?>?) {
        accommodationList = data
        notifyDataSetChanged()
    }
}
