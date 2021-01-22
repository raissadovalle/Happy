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

class CleaningAdapter(val context: Context) : RecyclerView.Adapter<CleaningAdapter.ViewHolder>() {

    var list: List<Cleaning> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleaningAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cleaning_item, parent, false)

        return CleaningAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CleaningAdapter.ViewHolder, position: Int) {
        val cleaningRoom = list[position]
        holder.descCleaningItem.text = cleaningRoom.desc
        //todo holder.memberCleaningItem.text = cleaningRoom.member.name
        holder.imageView.setImageResource(R.drawable.ic_baseline_home_24) //TODO mudança de icones
        holder.cardView.setOnClickListener {
            val intent = Intent(context, CleaningActivity::class.java)
            intent.putExtra("CLEANINGROOM", cleaningRoom)
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