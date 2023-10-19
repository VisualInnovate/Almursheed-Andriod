package com.visualinnovate.almursheed.commonView.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.ItemTextWrappedBinding

class TextAdapter(
    private val onItemClickCallBack: (item: ChooserItemModel, position: Int) -> Unit,
) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {

    private var items: List<ChooserItemModel> = ArrayList()

    private lateinit var binding: ItemTextWrappedBinding

    inner class ViewHolder(itemView: ItemTextWrappedBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val text = itemView.txtRating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemTextWrappedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = items[position]
        // bind view
        bindData(holder, order)
    }

    private fun bindData(holder: ViewHolder, item: ChooserItemModel) {
        holder.text.text = item.name
        if (item.isSelected) {
            val pL: Int = holder.text.paddingLeft
            val pT: Int = holder.text.paddingTop
            val pR: Int = holder.text.paddingRight
            val pB: Int = holder.text.paddingBottom
            holder.text.setBackgroundResource(R.drawable.bg_item_spinner_selected_with_ic_done)
            holder.text.setPadding(pL, pT, pR, pB)
        } else {
            val pL: Int = holder.text.paddingLeft
            val pT: Int = holder.text.paddingTop
            val pR: Int = holder.text.paddingRight
            val pB: Int = holder.text.paddingBottom
            holder.text.setBackgroundResource(R.drawable.bg_item_spinner_unselected)
            holder.text.setPadding(pL, pT, pR, pB)
        }

        holder.text.onDebouncedListener {
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
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
