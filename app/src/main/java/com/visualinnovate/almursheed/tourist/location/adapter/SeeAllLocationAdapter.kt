package com.visualinnovate.almursheed.tourist.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.databinding.ItemSeeAllLocationBinding
import com.visualinnovate.almursheed.home.model.AttractivesItem

class SeeAllLocationAdapter(
    private val btnLocationClickCallBack: (location: AttractivesItem) -> Unit
) : RecyclerView.Adapter<SeeAllLocationAdapter.SeeAllLocationViewHolder>() {

    private var newsList: List<AttractivesItem?>? = ArrayList()

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
                btnLocationClickCallBack.invoke(newsList!![adapterPosition]!!)
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
        val article = newsList!![position]!!
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: SeeAllLocationViewHolder, location: AttractivesItem) {
        // set data
//        Glide.with(holder.itemView.context)
//            .load(location.media?.get(0)?.originalUrl)
//            .into(holder.imglocation)
        holder.locationName.text = location.name?.localized
        holder.countryAndCity.text = location.country?.country
        /*// check favorite
        if (!location.locationFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }*/
    }

    override fun getItemCount(): Int {
        return newsList?.size ?: 0
    }

    fun submitData(data: List<AttractivesItem?>?) {
        newsList = data
        notifyDataSetChanged()
    }
}
