package com.visualinnovate.almursheed.guide.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.invisible
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ItemGuideBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.utils.Constant

class GuideAdapter(
    private val btnGuideClickCallBack: (guide: DriverAndGuideItem) -> Unit,
    private val onFavoriteClickCallBack: (guide: DriverAndGuideItem) -> Unit,
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private var guidesList: List<DriverAndGuideItem?>? = ArrayList()

    private lateinit var binding: ItemGuideBinding

    inner class GuideViewHolder(itemView: ItemGuideBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgGuide = itemView.imgGuide
        val imgFavorite = itemView.imgFavorite
        val imgStatus = itemView.imgStatus
        val username = itemView.username
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating
        private val btnBookNow = itemView.btnBookNow

        init {
            itemView.root.setOnClickListener {
                btnGuideClickCallBack.invoke(guidesList!![adapterPosition]!!)
            }

            btnBookNow.setOnClickListener {
                // btnGuideClickCallBack.invoke(guidesList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        binding = ItemGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guidesList!![position]
        // bind view
        bindData(holder, guide!!)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: GuideViewHolder, guide: DriverAndGuideItem) {
        holder.imgStatus.invisible()
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER
            || SharedPreference.getUserRole() == Constant.ROLE_GUIDES
        ) {
            binding.btnBookNow.gone()
            binding.imgFavorite.gone()
        } else {
            binding.btnBookNow.visible()
            binding.imgFavorite.visible()
        }

        // set data
        Glide.with(holder.itemView.context)
            .load(guide.personalPhoto)
            .into(holder.imgGuide)

        holder.username.text = guide.name
        holder.city.text = guide.stateName
        holder.rating.text = (guide.totalRating ?: 0.0).toString()

        guide.priceServices?.let {
            if (it.isNotEmpty()) {
                holder.price.text = "$" + it[0]?.price.toString()
            }else{
                holder.price.text = "$0.0"
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

        guide.priceServices?.let {
            if (it.isNotEmpty()) {
                holder.price.text = it[0]?.price.toString() + " $"
            }
        }
    }

    override fun getItemCount(): Int {
        return guidesList?.size ?: 0
    }

    fun submitData(data: List<DriverAndGuideItem?>?) {
        guidesList = data
        notifyDataSetChanged()
    }
}
