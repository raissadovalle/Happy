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
import com.example.happy.R
import com.example.happy.ShoppingActivity
import com.example.happy.model.Shopping

class ShoppingAdapter(val list: List<Shopping>, val context: Context) : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.shopping_item, parent, false)

        return ShoppingAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ShoppingAdapter.ViewHolder, position: Int) {
        val shopping = list[position]
        holder.descShoppingItem.text = shopping.desc
        holder.dateShoppingItem.text = shopping.date
        holder.cardView.setOnClickListener {
            val intent = Intent(context, ShoppingActivity::class.java)
            intent.putExtra("SHOPPING", shopping)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descShoppingItem: TextView = itemView.findViewById(R.id.desc_shopping_item)
        val dateShoppingItem: TextView = itemView.findViewById(R.id.date_shopping_item)
        val cardView: CardView = itemView.findViewById(R.id.cv_shopping_item)
    }
}