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
import com.example.happy.AddEditBillActivity
import com.example.happy.R
import com.example.happy.model.BillItem
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BillCloseMonthAdapter (val context: Context) : RecyclerView.Adapter<BillCloseMonthAdapter.ViewHolder>() {

    var list: MutableList<BillItem> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bill_close_month, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = list[position]
        holder.descBillItem.text = bill.desc
        val myPlace = Locale( "pt", "BR" )
        val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
        holder.priceBillItem.text = format.format(bill.price)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descBillItem: TextView = itemView.findViewById(R.id.desc_close_month)
        val priceBillItem: TextView = itemView.findViewById(R.id.price_close_month)
    }
}