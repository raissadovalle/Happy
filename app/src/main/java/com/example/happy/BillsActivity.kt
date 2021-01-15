package com.example.happy

import android.content.Intent
import com.example.happy.model.BillMonths
import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.BillType

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bill_item.*
import java.text.DateFormat
import java.util.*

class BillsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var chipGroupMonths: ChipGroup
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerBills: RecyclerView
    lateinit var cardViewBillItem: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.bills_toolbar_title)

        floatingButton = findViewById(R.id.fb_bills)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddEditBillActivity::class.java)
            startActivity(intent)
        }

//        cardViewBillItem = findViewById(R.id.cv_bill_item)
//        cardViewBillItem.setOnClickListener {
//            val intent = Intent(this, AddEditBillActivity::class.java)
//            startActivity(intent)
//        }

        recyclerBills = findViewById(R.id.rv_bills)

        val arrayBills = arrayListOf<BillItem>(BillItem(1, "Energia", 109.99, BillType(1, "Energia"), "12/01/2021"),
            BillItem(2, "Agua", 49.99, BillType(2,"Água"), "05/01/2021"),
            BillItem(3, "Aluguel", 799.90,BillType(3,"Aluguel"), "03/01/2021"),
            BillItem(4, "Amaciante", 10.90, BillType(1,"Compras"), "01/01/2021"))

        val adapterBills = BillItemAdapter(arrayBills, this)

        recyclerBills.adapter = adapterBills
        recyclerBills.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)

        chipGroupMonths = findViewById(R.id.chip_group_months)
        fillChipMonths()
    }

    fun fillChipMonths() {

        val months = arrayListOf(BillMonths("Dez", "2020"), BillMonths("Jan", "2021"))

        for (month in months) {
            val chip = Chip(ContextThemeWrapper(chipGroupMonths.context, R.style.Widget_MaterialComponents_Chip_Choice))
            chip.text = month.month + "/" + month.year
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_gray))
            chip.chipStrokeWidth = 1.0F
            chip.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_gray))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_green)))
            chipGroupMonths.addView(chip)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}