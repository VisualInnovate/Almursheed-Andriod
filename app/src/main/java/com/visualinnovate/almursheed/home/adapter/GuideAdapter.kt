package com.visualinnovate.almursheed.home.adapter

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
import com.visualinnovate.almursheed.databinding.ItemDriverBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.utils.Constant

class GuideAdapter(
    private val btnGuideClickCallBack: (guide: DriverAndGuideItem) -> Unit,
    private val onFavoriteClickCallBack: (guide: DriverAndGuideItem) -> Unit,
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private var guidesList: List<DriverAndGuideItem?>? = ArrayList()

    private lateinit var binding: ItemDriverBinding

    inner class GuideViewHolder(itemView: ItemDriverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgDriver = itemView.imgDriver
        val imgFavorite = itemView.imgFavorite
        val imgStatus = itemView.imgStatus
        val username = itemView.username
        val price = itemView.price
        val city = itemView.city
        val rating = itemView.rating
        private val btnBookNow = itemView.btnBookNow

        init {
            btnBookNow.setOnClickListener {
                btnGuideClickCallBack.invoke(guidesList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        binding = ItemDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guidesList!![position]
        // bind view
        bindData(holder, guide!!)
    }

    private fun bindData(holder: GuideViewHolder, guide: DriverAndGuideItem) {
        holder.imgStatus.invisible()
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER) {
            binding.btnBookNow.gone()
        } else {
            binding.btnBookNow.visible()
        }
        // set data
        Glide.with(holder.itemView.context)
            .load(guide.imageBackground)
            .into(holder.imgDriver)
        holder.username.text = guide.name
        holder.city.text = guide.stateName
        holder.rating.text = guide.totalRating

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
        return guidesList?.size ?: 0
    }

    fun submitData(data: List<DriverAndGuideItem?>?) {
        guidesList = data
        notifyDataSetChanged()
    }
}
