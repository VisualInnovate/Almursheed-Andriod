package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemBannerBinding
import com.visualinnovate.almursheed.home.model.BannersItem

class BannerAdapter(
    private val btnBannerClickCallBack: (bannerModel: BannersItem) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private var newsList: List<BannersItem> = ArrayList()

    private lateinit var binding: ItemBannerBinding

    inner class BannerViewHolder(itemView: ItemBannerBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgBanner = itemView.imgBanner

        init {
            itemView.root.setOnClickListener {
                btnBannerClickCallBack.invoke(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val article = newsList[position]
        // bind view
        bindData(holder, article)
    }

    private fun bindData(holder: BannerViewHolder, banner: BannersItem) {
        Glide.with(holder.itemView.context)
            .load(banner.pictures?.photos?.get(0)?.originalUrl ?: "")
            .into(binding.imgBanner)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<BannersItem>) {
        newsList = data
        notifyDataSetChanged()
    }
}
