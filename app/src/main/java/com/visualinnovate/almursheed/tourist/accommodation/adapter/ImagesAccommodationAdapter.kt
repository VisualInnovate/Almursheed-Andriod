package com.visualinnovate.almursheed.tourist.accommodation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAccommodationImageBinding
import com.visualinnovate.almursheed.home.model.MediaItem

class ImagesAccommodationAdapter(
    private val btnImageItemClickCallBack: (image: MediaItem) -> Unit
) : RecyclerView.Adapter<ImagesAccommodationAdapter.AccommodationImagesViewHolder>() {

    private var imagesList: ArrayList<MediaItem?>? = ArrayList()
    private var imagesStringList: ArrayList<String?>? = ArrayList()

    private lateinit var binding: ItemAccommodationImageBinding

    inner class AccommodationImagesViewHolder(itemView: ItemAccommodationImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val image = itemView.image
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
        val image = imagesList?.get(position)
        // bind view
        bindData(holder, image)
    }

    private fun bindData(
        holder: AccommodationImagesViewHolder,
        image: MediaItem?
    ) {
        // set data
        Glide.with(holder.itemView.context)
            .load(image?.originalUrl)
            .error(R.drawable.ic_mursheed_logo)
            .into(holder.image)

        holder.image.setOnClickListener {
            btnImageItemClickCallBack.invoke(image ?: MediaItem())
        }
    }

    override fun getItemCount(): Int {
        return imagesList?.size ?: 0
    }

    fun submitData(data: ArrayList<MediaItem?>?) {
        imagesList = data
        notifyDataSetChanged()
    }

    fun submitStringData(data: ArrayList<String?>?) {
        imagesStringList = data
        notifyDataSetChanged()
    }
}
