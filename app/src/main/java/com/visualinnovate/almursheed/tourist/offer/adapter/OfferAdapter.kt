package com.visualinnovate.almursheed.tourist.offer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.databinding.ItemOfferBinding
import com.visualinnovate.almursheed.home.model.OfferItem
import com.visualinnovate.almursheed.utils.Constant

class OfferAdapter(
    private val btnBoobNowOfferCallBack: (offer: OfferItem) -> Unit,
    private val btnDetailsOfferCallBack: (offer: OfferItem) -> Unit
) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    private var offerList: List<OfferItem?>? = ArrayList()

    private lateinit var binding: ItemOfferBinding

    inner class OfferViewHolder(itemView: ItemOfferBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgOffer = itemView.imgOffer
        val offerName = itemView.offerName
        val offerRating = itemView.offerRating
        private val btnBookNow = itemView.btnBookNow
        private val details = itemView.details

        init {
            btnBookNow.setOnClickListener {
                btnBoobNowOfferCallBack.invoke(offerList!![adapterPosition]!!)
            }
            details.setOnClickListener {
                btnDetailsOfferCallBack.invoke(offerList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        binding =
            ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val flight = offerList!![position]!!
        // bind view
        bindData(holder, flight)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: OfferViewHolder, offer: OfferItem) {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER) {
            binding.btnBookNow.gone()
        } else {
            binding.btnBookNow.visible()
        }

        // set data
        // Utils.loadImage(holder.itemView.context, offer.offerImage, holder.imgOffer)
        Utils.loadImage(holder.itemView.context, R.drawable.img_test, holder.imgOffer)
        // holder.imgFlight.setImageResource(flight.flightImage)
        holder.offerName.text = offer.title?.localized ?: ""
        holder.offerRating.text = offer.number ?: ""
    }

    override fun getItemCount(): Int {
        return offerList?.size ?: 0
    }

    fun submitData(data: List<OfferItem?>?) {
        offerList = data
        notifyDataSetChanged()
    }
}
