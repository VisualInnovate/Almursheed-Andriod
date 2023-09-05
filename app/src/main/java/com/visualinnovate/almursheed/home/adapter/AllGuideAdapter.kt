package com.visualinnovate.almursheed.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAllGuideBinding
import com.visualinnovate.almursheed.home.model.GuidesItem

class AllGuideAdapter(
    private val btnAccommodationClickCallBack: (guide: GuidesItem) -> Unit
) : RecyclerView.Adapter<AllGuideAdapter.AllGuideViewHolder>() {

    private var allGuideList: List<GuidesItem?>? = ArrayList()

    private lateinit var binding: ItemAllGuideBinding

    inner class AllGuideViewHolder(itemView: ItemAllGuideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgGuide = itemView.imgGuide
        val imgFavorite = itemView.imgFavorite
        val guideRating = itemView.guideRating
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
    private fun bindData(holder: AllGuideViewHolder, guide: GuidesItem) {
        // set data
        // Utils.loadImage(holder.itemView.context, accommodation.imageBanner, holder.imgBanner)
        Glide.with(holder.itemView.context)
            // .load(driver.pictures?.personalPictures?.get(position)?.originalUrl)
            .load(R.drawable.img_driver)
            .into(holder.imgGuide)
        holder.guideName.text = guide.name
        // holder.price.text = "$ ${guide.guidePrice}"
        // holder.city.text = guide.guideCity
        // holder.language.text = guide.language
        // check favorite
        /*if (!guide.guideFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }*/
    }

    override fun getItemCount(): Int {
        return allGuideList?.size ?: 0
    }

    fun submitData(data: List<GuidesItem?>?) {
        allGuideList = data
        notifyDataSetChanged()
    }
}
