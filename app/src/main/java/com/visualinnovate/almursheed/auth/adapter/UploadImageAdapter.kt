package com.visualinnovate.almursheed.auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemUploadImageBinding

class UploadImageAdapter : RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder>() {

    private var imageList: List<Int> = ArrayList()

    private lateinit var binding: ItemUploadImageBinding

    inner class UploadImageViewHolder(itemView: ItemUploadImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgUpload = itemView.imgUpload
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageViewHolder {
        binding =
            ItemUploadImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadImageViewHolder, position: Int) {
        val image = imageList[position]
        // bind view
        bindData(holder, image)
    }

    private fun bindData(holder: UploadImageViewHolder, image: Int) {
        // set data
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.imgUpload)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun submitData(data: List<Int>) {
        imageList = data
        notifyDataSetChanged()
    }
}
