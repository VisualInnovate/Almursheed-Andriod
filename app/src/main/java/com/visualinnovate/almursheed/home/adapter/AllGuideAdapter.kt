package com.visualinnovate.almursheed.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAllGuideBinding
import com.visualinnovate.almursheed.home.model.GuideModel

class AllGuideAdapter(
    private val btnAccommodationClickCallBack: (accommodation: GuideModel) -> Unit
) : RecyclerView.Adapter<AllGuideAdapter.AllGuideViewHolder>() {

    private var newsList: List<GuideModel> = ArrayList()

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
                btnAccommodationClickCallBack.invoke(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllGuideViewHolder {
        binding =
            ItemAllGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllGuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllGuideViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: AllGuideViewHolder, guide: GuideModel) {
        // set data
        // Utils.loadImage(holder.itemView.context, accommodation.imageBanner, holder.imgBanner)
        holder.imgGuide.setImageResource(guide.guideImage)
        holder.guideRating.text = guide.guideRating.toString()
        holder.guideName.text = guide.guideName
        holder.price.text = "$ ${guide.guidePrice}"
        holder.city.text = guide.guideCity
        holder.language.text = guide.language
        // check favorite
        if (!guide.guideFavorite) { // false -> un favorite
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<GuideModel>) {
        newsList = data
        notifyDataSetChanged()
    }
}
