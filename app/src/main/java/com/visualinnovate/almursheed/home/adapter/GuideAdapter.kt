package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.invisible
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.home.model.GuideModel

class GuideAdapter(
    private val btnGuideClickCallBack: (guideModel: GuideModel) -> Unit
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private var newsList: List<GuideModel> = ArrayList()

    private lateinit var binding: ItemDriverBinding

    inner class GuideViewHolder(itemView: ItemDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgBanner = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val imgStatus = itemView.imgStatus
        val username = itemView.username
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating
        private val btnBookNow = itemView.btnBookNow

        init {
            btnBookNow.setOnClickListener {
                btnGuideClickCallBack.invoke(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        binding = ItemDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: GuideViewHolder, guide: GuideModel) {
        holder.imgStatus.invisible()
        // set data
        // Utils.loadImage(holder.itemView.context, guide.imageBanner, holder.imgBanner)
        holder.rating.text = guide.guideRating.toString()
        holder.username.text = guide.guideName
        holder.price.text = guide.guidePrice.toString()
        holder.city.text = guide.guideCity
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
