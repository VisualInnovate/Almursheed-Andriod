package com.visualinnovate.almursheed.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.common.Utils
import com.visualinnovate.almursheed.databinding.ItemOfferBinding
import com.visualinnovate.almursheed.home.model.OfferModel

class OfferAdapter(
    private val btnBoobNowOfferCallBack: (offer: OfferModel) -> Unit,
    private val btnDetailsOfferCallBack: (offer: OfferModel) -> Unit
) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    private var offerList: List<OfferModel> = ArrayList()

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
                btnBoobNowOfferCallBack.invoke(offerList[adapterPosition])
            }
            details.setOnClickListener {
                btnDetailsOfferCallBack.invoke(offerList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        binding =
            ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val flight = offerList[position]
        // bind view
        bindData(holder, flight)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: OfferViewHolder, offer: OfferModel) {
        // set data
        Utils.loadImage(holder.itemView.context, offer.offerImage, holder.imgOffer)
        // holder.imgFlight.setImageResource(flight.flightImage)
        holder.offerName.text = offer.offerName
        holder.offerRating.text = offer.offerRating.toString()
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    fun submitData(data: List<OfferModel>) {
        offerList = data
        notifyDataSetChanged()
    }
}
