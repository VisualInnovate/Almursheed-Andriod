package com.visualinnovate.almursheed.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemAccommodationImageBinding

class AccommodationImagesAdapter(
    private val btnAccommodationClickCallBack: (image: String) -> Unit
) : RecyclerView.Adapter<AccommodationImagesAdapter.AccommodationImagesViewHolder>() {

    private var imagesList: List<String> = ArrayList()

    private lateinit var binding: ItemAccommodationImageBinding

    inner class AccommodationImagesViewHolder(itemView: ItemAccommodationImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgAccommodation = itemView.imgAccommodation

        init {
            itemView.root.setOnClickListener {
                btnAccommodationClickCallBack.invoke(imagesList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccommodationImagesViewHolder {
        binding =
            ItemAccommodationImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AccommodationImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccommodationImagesViewHolder, position: Int) {
        val image = imagesList[position]
        // bind view
        bindData(holder, image)
    }

    private fun bindData(
        holder: AccommodationImagesViewHolder,
        image: String
    ) {
        // set data
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.imgAccommodation)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    fun submitData(data: ArrayList<String>) {
        imagesList = data
        notifyDataSetChanged()
    }
}
