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
import com.example.happy.AddEditBillActivity
import com.example.happy.R
import com.example.happy.model.BillItem
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class BillItemAdapter(val context: Context) : RecyclerView.Adapter<BillItemAdapter.ViewHolder>() {

    var list: MutableList<BillItem> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = list[position]
        holder.descBillItem.text = bill.desc
        val myPlace = Locale( "pt", "BR" )
        val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
        holder.priceBillItem.text = format.format(bill.price)
        if(bill.isClosed)holder.priceBillItem.paintFlags = holder.priceBillItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        if(bill.type == BillItem.BillType.RENT) holder.imageView.setImageResource(R.drawable.ic_baseline_home_24)
        if(bill.type == BillItem.BillType.ELECTRICITY) holder.imageView.setImageResource(R.drawable.ic_baseline_battery_charging_full_24)
        if(bill.type == BillItem.BillType.WATER) holder.imageView.setImageResource(R.drawable.ic_baseline_invert_colors_24)
        if(bill.type == BillItem.BillType.SHOPPING) holder.imageView.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
        if(bill.type == BillItem.BillType.OTHERS) holder.imageView.setImageResource(R.drawable.ic_baseline_attach_money_24)

        holder.cardView.setOnClickListener {
            val intent = Intent(context, AddEditBillActivity::class.java)
            intent.putExtra("IS_EDIT_BILL", true)
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
