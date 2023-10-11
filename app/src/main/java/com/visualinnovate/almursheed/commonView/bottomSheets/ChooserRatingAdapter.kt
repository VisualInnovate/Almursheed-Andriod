package com.visualinnovate.almursheed.commonView.bottomSheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.ItemImageMatchBinding

class ChooserRatingAdapter(
    private val onItemClickCallBack: (item: ChooserItemModel, position: Int) -> Unit,
) : RecyclerView.Adapter<ChooserRatingAdapter.ViewHolder>() {

    private var items: List<ChooserItemModel> = ArrayList()

    private lateinit var binding: ItemImageMatchBinding

    inner class ViewHolder(itemView: ItemImageMatchBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val background = itemView.background
        val image = itemView.image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemImageMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = items[position]
        // bind view
        bindData(holder, order, position)
    }

    private fun bindData(holder: ViewHolder, item: ChooserItemModel, position: Int) {
        if (position == 0) {
            holder.background.text = item.name
        } else {
            holder.image.setImageResource(item.name!!.toInt())
        }

        if (item.isSelected) {
            val pL: Int = holder.background.paddingLeft
            val pT: Int = holder.background.paddingTop
            val pR: Int = holder.background.paddingRight
            val pB: Int = holder.background.paddingBottom
            holder.background.setBackgroundResource(R.drawable.bg_item_spinner_selected_with_ic_done)
            holder.background.setPadding(pL, pT, pR, pB)
        } else {
            val pL: Int = holder.background.paddingLeft
            val pT: Int = holder.background.paddingTop
            val pR: Int = holder.background.paddingRight
            val pB: Int = holder.background.paddingBottom
            holder.background.setBackgroundResource(R.drawable.bg_item_spinner_unselected)
            holder.background.setPadding(pL, pT, pR, pB)
        }

        holder.background.onDebouncedListener {
            handleSelectedItem(item)
            onItemClickCallBack.invoke(item, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(data: ArrayList<ChooserItemModel>) {
        items = data
        notifyDataSetChanged()
    }

    private fun handleSelectedItem(item: ChooserItemModel) {
        items.forEach {
            it.isSelected = item.name == it.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
