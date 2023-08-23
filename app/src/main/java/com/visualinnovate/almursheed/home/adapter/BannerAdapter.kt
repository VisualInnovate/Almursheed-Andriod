package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.common.Utils
import com.visualinnovate.almursheed.databinding.ItemBannerBinding
import com.visualinnovate.almursheed.home.model.BannerModel

class BannerAdapter(
    private val btnBannerClickCallBack: (bannerModel: BannerModel) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private var newsList: List<BannerModel> = ArrayList()

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

    private fun bindData(holder: BannerViewHolder, banner: BannerModel) {
        Utils.loadImage(holder.itemView.context, banner.imageBanner, holder.imgBanner)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitData(data: List<BannerModel>) {
        newsList = data
        notifyDataSetChanged()
    }
}
