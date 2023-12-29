package com.visualinnovate.almursheed.tourist.accommodation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemAccommodationBinding
import com.visualinnovate.almursheed.home.model.AccommodationItem
import com.visualinnovate.almursheed.utils.Constant

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
        val imgGoogleMap = itemView.imgGoogleMap
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccommodationViewHolder {
        binding =
            ItemAccommodationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccommodationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccommodationViewHolder, position: Int) {
        val accommodation = accommodationList!![position]!!
        // bind view
        bindData(holder, accommodation)
    }

    private fun bindData(
        holder: AccommodationViewHolder,
        accommodation: AccommodationItem
    ) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER
            || SharedPreference.getUserRole() == Constant.ROLE_GUIDES
        ) {
            binding.imgFavorite.gone()
        } else {
            binding.imgFavorite.visible()
        }

        // set data
        Glide.with(holder.itemView.context)
            .load(accommodation.pictures?.photos?.get(0)?.originalUrl)
            .error(R.drawable.ic_mursheed_logo)
            .into(holder.imgAccommodation)

        holder.accommodationName.text = accommodation.name?.localized ?: ""
        holder.countryAndCity.text = accommodation.country?.country ?: ""

        // check favorite
        /*if (!accommodation.isFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite)
        }*/

        holder.itemView.setOnClickListener {
            btnAccommodationClickCallBack.invoke(accommodation)
        }

        holder.imgGoogleMap.setOnClickListener {
            openGoogleMaps(holder.itemView.context, accommodation.address?.localized ?: "")
        }
    }

    override fun getItemCount(): Int {
        return accommodationList?.size ?: 0
    }

    fun submitData(data: List<AccommodationItem?>?) {
        accommodationList = data
        notifyDataSetChanged()
    }

    private fun openGoogleMaps(context: Context, mapUrl: String) {
        val intentUri = Uri.parse(mapUrl)
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)

        // Check if there's an app to handle the intent
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Handle the case where Google Maps app is not installed
            // or there's no app to handle the intent
        }
    }
}
