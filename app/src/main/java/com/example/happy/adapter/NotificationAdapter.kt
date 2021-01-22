package com.example.happy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.CleaningActivity
import com.example.happy.R
import com.example.happy.model.Cleaning
import com.example.happy.model.Notification

class NotificationAdapter (val context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var list: List<Notification> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)

        return NotificationAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val notification = list[position]
        holder.contentNotificationItem.text = notification.content
        holder.imageView.setImageResource(R.drawable.ic_baseline_home_24) //TODO mudança de icones pelos components
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_notification_item)
        val contentNotificationItem: TextView = itemView.findViewById(R.id.content_notification_item)
        val cardView: CardView = itemView.findViewById(R.id.cv_notification_item)
    }
}