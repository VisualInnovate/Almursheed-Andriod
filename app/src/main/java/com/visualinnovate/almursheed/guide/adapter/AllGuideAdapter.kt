package com.visualinnovate.almursheed.guide.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemAllGuideBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.utils.Constant

class AllGuideAdapter(
    private val btnAccommodationClickCallBack: (guide: DriverAndGuideItem) -> Unit,
    private val onFavoriteClickCallBack: (guide: DriverAndGuideItem) -> Unit,
) : RecyclerView.Adapter<AllGuideAdapter.AllGuideViewHolder>() {

    private var allGuideList: List<DriverAndGuideItem?>? = ArrayList()

    private lateinit var binding: ItemAllGuideBinding

    inner class AllGuideViewHolder(itemView: ItemAllGuideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgGuide = itemView.imgGuide
        val imgFavorite = itemView.imgFavorite
        val rating = itemView.rating
        val guideName = itemView.guideName
        val price = itemView.price
        val city = itemView.city
        val language = itemView.language

        init {
            itemView.root.setOnClickListener {
                btnAccommodationClickCallBack.invoke(allGuideList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllGuideViewHolder {
        binding =
            ItemAllGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllGuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllGuideViewHolder, position: Int) {
        val guide = allGuideList!![position]!!
        // bind view
        bindData(holder, guide)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: AllGuideViewHolder, guide: DriverAndGuideItem) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER
            || SharedPreference.getUserRole() == Constant.ROLE_GUIDES
        ) {
            binding.imgFavorite.gone()
        } else {
            binding.imgFavorite.visible()
        }

        // set data
        Glide.with(holder.itemView.context)
            .load(guide.imageBackground)
            .into(holder.imgGuide)
        holder.guideName.text = guide.name
        holder.city.text = guide.stateName
        // holder.language.text = guide.language
        holder.rating.text = (guide.totalRating ?: 0.0).toString()

        guide.priceServices?.let {
            if (it.isNotEmpty()) {
                holder.price.text = it[0]?.price.toString() +" $"
            }
        }
        // check favorite
        if (guide.isFavourite == false) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite)
        }

        holder.imgFavorite.onDebouncedListener {
            onFavoriteClickCallBack.invoke(guide)
        }
    }

    override fun getItemCount(): Int {
        return allGuideList?.size ?: 0
    }

    fun submitData(data: List<DriverAndGuideItem?>?) {
        allGuideList = data
        notifyDataSetChanged()
    }
}
