package com.visualinnovate.almursheed.tourist.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.ItemAttractiveImageBinding

class ImagesAttractiveAdapter(
    private val btnImageItemClickCallBack: (image: String) -> Unit
) : RecyclerView.Adapter<ImagesAttractiveAdapter.AttractiveImagesViewHolder>() {

    private var imagesList: ArrayList<String?>? = ArrayList()

    private lateinit var binding: ItemAttractiveImageBinding

    inner class AttractiveImagesViewHolder(itemView: ItemAttractiveImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val image = itemView.image
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttractiveImagesViewHolder {
        binding =
            ItemAttractiveImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AttractiveImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttractiveImagesViewHolder, position: Int) {
        val image = imagesList?.get(position)
        // bind view
        bindData(holder, image)
    }

    private fun bindData(
        holder: AttractiveImagesViewHolder,
        image: String?
    ) {
        // set data
        Glide.with(holder.itemView.context)
            .load(image)
            .error(R.drawable.ic_mursheed_logo)
            .into(holder.image)

        holder.image.setOnClickListener {
            btnImageItemClickCallBack.invoke(image ?: "")
        }
    }

    override fun getItemCount(): Int {
        return imagesList?.size ?: 0
    }

    fun submitData(data: ArrayList<String?>?) {
        imagesList = data
        notifyDataSetChanged()
    }
}
