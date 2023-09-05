package com.visualinnovate.almursheed.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.databinding.ItemFlightBinding
import com.visualinnovate.almursheed.home.model.FlightItem

class FlightAdapter(
    private val btnBoobNowFlightCallBack: (flight: FlightItem) -> Unit
) : RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    //    private var flightList: List<FlightModel> = ArrayList()
    private var flightItemList: List<FlightItem?>? = ArrayList()

    private lateinit var binding: ItemFlightBinding

    inner class FlightViewHolder(itemView: ItemFlightBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgFlight = itemView.imgFlight
        val flightName = itemView.flightName
        val flightDiscount = itemView.flightDiscount
        private val btnBookNow = itemView.btnBookNow

        init {
            btnBookNow.setOnClickListener {
                btnBoobNowFlightCallBack.invoke(flightItemList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        binding =
            ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flightItemList!![position]!!
        // bind view
        bindData(holder, flight)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: FlightViewHolder, flight: FlightItem) {
        // set data
        // Utils.loadImage(holder.itemView.context, flight.flightImage, holder.imgFlight)
        Utils.loadImage(holder.itemView.context, R.drawable.ic_egypt_air, holder.imgFlight)
        // holder.imgFlight.setImageResource(flight.flightImage)
        holder.flightName.text = flight.name?.localized
        holder.flightDiscount.text = "Discount ${flight.discount}%"
    }

    override fun getItemCount(): Int {
        return flightItemList?.size ?: 0
    }

//    fun submitData(data: List<FlightModel>) {
//        flightList = data
//        notifyDataSetChanged()
//    }

    fun submitData(data: List<FlightItem?>?) {
        flightItemList = data
        notifyDataSetChanged()
    }
}
