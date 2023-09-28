package com.visualinnovate.almursheed.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.myOrders.models.OrderDetailsItem
import com.visualinnovate.almursheed.databinding.ItemDaysBinding
import com.visualinnovate.almursheed.utils.Utils

class DaysAdapter(
    private val selectDaysCallback: ((day: String, cityId: Int) -> Unit)? = null,
    private val enableEdit: Boolean = true,
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    private var days: List<DayModel> = ArrayList()
    private var orderDetailsList: List<OrderDetailsItem?>? = ArrayList()

    private lateinit var binding: ItemDaysBinding

    inner class DaysViewHolder(itemView: ItemDaysBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val dayNumber = itemView.dayNumber
        private val btnCollapse = itemView.btnCollapse
        private val routeView = itemView.routeView

        // val destination = itemView.destination
        val txtDestination = itemView.txtDestination
        val spinnerCity = itemView.spinnerCity

        init {
            btnCollapse.onDebouncedListener {
                if (routeView.isVisible) {
                    btnCollapse.setBackgroundResource(R.drawable.bg_plus_curved_green)
                    routeView.gone()
                    // spinnerCity.getRoot().gone()
                } else {
                    btnCollapse.setBackgroundResource(R.drawable.bg_minus_curved_green)
                    routeView.visible()
                    // spinnerCity.getRoot().visible()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        binding = ItemDaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val day = days[position]
        val orderDetails = orderDetailsList?.get(position)
        // bind view
        bindData(holder, day, orderDetails)
    }

    private fun bindData(holder: DaysViewHolder, day: DayModel, orderDetails: OrderDetailsItem?) {
        holder.dayNumber.text = day.date

        if (enableEdit) {
            initCitySpinner(holder.itemView.context, holder, day)
            holder.txtDestination.gone()
            holder.spinnerCity.root.visible()
        } else {
            holder.spinnerCity.root.gone()
            holder.txtDestination.visible()
            holder.txtDestination.text = orderDetails?.state ?: ""
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun submitData(data: ArrayList<DayModel>, orders: List<OrderDetailsItem?>?) {
        days = data
        orderDetailsList = orders
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private var cityId: Int? = null
    private fun initCitySpinner(context: Context, holder: DaysViewHolder, day: DayModel) {
        val cityList = Utils.cities.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cityList,
            )

        holder.spinnerCity.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        holder.spinnerCity.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    // Retrieve the selected country name
                    val selectedCityName = cityList[position]
                    cityId = Utils.cities[selectedCityName]!!
                    selectDaysCallback?.invoke(day.date.toString(), cityId!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }
}
