package com.visualinnovate.almursheed.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.common.Utils
import com.visualinnovate.almursheed.databinding.ItemFlightBinding
import com.visualinnovate.almursheed.home.model.FlightModel

class FlightAdapter(
    private val btnBoobNowFlightCallBack: (flight: FlightModel) -> Unit
) : RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    private var flightList: List<FlightModel> = ArrayList()

    private lateinit var binding: ItemFlightBinding

    inner class FlightViewHolder(itemView: ItemFlightBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgFlight = itemView.imgFlight
        val flightName = itemView.flightName
        val flightDiscount = itemView.flightDiscount
        private val btnBookNow = itemView.btnBookNow

        init {
            btnBookNow.setOnClickListener {
                btnBoobNowFlightCallBack.invoke(flightList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        binding =
            ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flightList[position]
        // bind view
        bindData(holder, flight)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: FlightViewHolder, flight: FlightModel) {
        // set data
        Utils.loadImage(holder.itemView.context, flight.flightImage, holder.imgFlight)
        // holder.imgFlight.setImageResource(flight.flightImage)
        holder.flightName.text = flight.flightName
        holder.flightDiscount.text = "Discount ${flight.flightDiscount}"
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    fun submitData(data: List<FlightModel>) {
        flightList = data
        notifyDataSetChanged()
    }
}
