package com.example.happy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.AddCleaningRoomActivity
import com.example.happy.CleaningActivity
import com.example.happy.R
import com.example.happy.model.Cleaning

class CleaningAdapter(val context: Context) : RecyclerView.Adapter<CleaningAdapter.ViewHolder>() {

    var list: MutableList<Cleaning> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleaningAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cleaning_item, parent, false)

        return CleaningAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CleaningAdapter.ViewHolder, position: Int) {
        val cleaningRoom = list[position]
        holder.descCleaningItem.text = cleaningRoom.desc
        holder.memberCleaningItem.text = cleaningRoom.memberName
        if(cleaningRoom.wasCleaned) {
            holder.descCleaningItem.paintFlags = holder.descCleaningItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.memberCleaningItem.paintFlags = holder.memberCleaningItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        if(cleaningRoom.desc.contains("sala", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_living_room)
        else if(cleaningRoom.desc.contains("quarto", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_bed)
        else if(cleaningRoom.desc.contains("cozinha", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_kitchen)
        else if(cleaningRoom.desc.contains("banheiro", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_toilet)
        else if(cleaningRoom.desc.contains("quintal", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_fence)
        else if(cleaningRoom.desc.contains("garagem", ignoreCase = true)) holder.imageView.setImageResource(R.drawable.ic_garage)
        else holder.imageView.setImageResource(R.drawable.ic_baseline_home_24)
        holder.cardView.setOnClickListener {
            val intent = Intent(context, AddCleaningRoomActivity::class.java)
            intent.putExtra("IS_EDIT_CLEANING", true)
            intent.putExtra("CLEANING", cleaningRoom)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_cleaning_item)
        val descCleaningItem: TextView = itemView.findViewById(R.id.desc_cleaning_item)
        val memberCleaningItem: TextView = itemView.findViewById(R.id.member_cleaning_item)
        val cardView: CardView = itemView.findViewById(R.id.cv_cleaning_item)
    }
}