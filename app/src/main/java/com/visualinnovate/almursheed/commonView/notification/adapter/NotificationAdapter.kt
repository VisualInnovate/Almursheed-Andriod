package com.visualinnovate.almursheed.commonView.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.visualinnovate.almursheed.commonView.notification.model.NotificationItem
import com.visualinnovate.almursheed.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val btnNotificationItemClickCallBack: (notificationItem: NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notificationList: List<NotificationItem?>? = ArrayList()

    private lateinit var binding: ItemNotificationBinding

    inner class NotificationViewHolder(itemView: ItemNotificationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val imgUser = itemView.imgUser
        val userName = itemView.userName
        val content = itemView.content

        init {
            itemView.root.setOnClickListener {
                btnNotificationItemClickCallBack.invoke(notificationList!![adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notificationItem = notificationList!![position]
        // bind view
        bindData(holder, notificationItem!!)
    }

    private fun bindData(holder: NotificationViewHolder, notificationItem: NotificationItem) {
        /*Glide.with(holder.itemView.context)
            .load(banner.pictures?.photos?.get(0)?.originalUrl ?: "")
            .into(binding.imgBanner)*/

        binding.content.text = notificationItem.notification ?: ""
    }

    override fun getItemCount(): Int {
        return notificationList?.size ?: 0
    }

    fun submitData(data: List<NotificationItem?>?) {
        notificationList = data
        notifyDataSetChanged()
    }
}
