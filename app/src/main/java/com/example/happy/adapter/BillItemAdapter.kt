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
import com.example.happy.BillsActivity
import com.example.happy.R
import com.example.happy.model.BillItem

class BillItemAdapter(val context: Context) : RecyclerView.Adapter<BillItemAdapter.ViewHolder>() {

    var list: List<BillItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = list[position]
        holder.descBillItem.text = bill.desc
        holder.priceBillItem.text = bill.price.toString()
        holder.imageView.setImageResource(R.drawable.ic_baseline_attach_money_24) //TODO mudança de icones
        holder.cardView.setOnClickListener {
            val intent = Intent(context, BillsActivity::class.java)
            intent.putExtra("BILL", bill)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_bill_item)
        val descBillItem: TextView = itemView.findViewById(R.id.desc_bill_item)
        val priceBillItem: TextView = itemView.findViewById(R.id.price_bill_item)
        val cardView: CardView = itemView.findViewById(R.id.cv_bill_item)
    }
}
