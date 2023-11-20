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
import com.visualinnovate.almursheed.commonView.price.models.PriceItem
import com.visualinnovate.almursheed.databinding.ItemDaysBinding

class PricesCitesAdapter(
    private val selectDaysCallback: ((day: String, cityId: Int, cityName:String?) -> Unit)? = null,
) : RecyclerView.Adapter<PricesCitesAdapter.PricesCitesViewHolder>() {

    private var citesList: List<PriceItem?> = ArrayList()
    private var citesListString: ArrayList<String> = ArrayList()
    private var days: List<DayModel> = ArrayList()
    private var cityId: Int? = null

    private lateinit var binding: ItemDaysBinding

    inner class PricesCitesViewHolder(itemView: ItemDaysBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val dayNumber = itemView.dayNumber
        val btnCollapse = itemView.btnCollapse
        val routeView = itemView.routeView

        // val txtDestination = itemView.txtDestination
        val spinnerCity = itemView.spinnerCity

        init {
            btnCollapse.onDebouncedListener {
                if (spinnerCity.getRoot().isVisible) {
                    btnCollapse.setBackgroundResource(R.drawable.bg_plus_curved_green)
                    // routeView.gone()
                    spinnerCity.getRoot().gone()
                } else {
                    btnCollapse.setBackgroundResource(R.drawable.bg_minus_curved_green)
                    // routeView.visible()
                    spinnerCity.getRoot().visible()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricesCitesViewHolder {
        binding = ItemDaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PricesCitesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PricesCitesViewHolder, position: Int) {
        val day = days[position]
        bindData(holder, day)
    }

    private fun bindData(holder: PricesCitesViewHolder, day: DayModel) {
        holder.dayNumber.text = day.date

        initCitySpinner(holder.itemView.context, holder, day)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun submitData(daysList: ArrayList<DayModel>, cities: ArrayList<PriceItem>) {
        citesList = cities
        citesListString.clear()
        cities.forEach {
            citesListString.add(it?.cityName.toString())
        }
        days = daysList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun initCitySpinner(
        context: Context,
        holder: PricesCitesViewHolder,
        day: DayModel,
    ) {

        val arrayAdapter =
            ArrayAdapter(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                citesListString,
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
                    val selectedCityName = citesList[position]
                    cityId = selectedCityName?.cityId
                    day.cityId = cityId
                    selectDaysCallback?.invoke(day.date.toString(), cityId!! , selectedCityName?.cityName)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }
}
