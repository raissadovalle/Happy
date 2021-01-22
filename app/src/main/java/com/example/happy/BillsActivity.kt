package com.example.happy

import android.content.Intent
import com.example.happy.model.BillMonths
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.repository.BillRepository
import com.example.happy.viewmodel.BillViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BillsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var chipGroupMonths: ChipGroup
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerBills: RecyclerView
    private val billViewModel by viewModels<BillViewModel>()

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

        recyclerBills = findViewById(R.id.rv_bills)

        val adapterBills = BillItemAdapter(this)

        billViewModel.getBillByRepId("1").observe(this, Observer{
            adapterBills.list = it
            adapterBills.notifyDataSetChanged()

            chipGroupMonths = findViewById(R.id.chip_group_months)
            fillChipMonths()
        }) //TODO repid


        recyclerBills.adapter = adapterBills
        recyclerBills.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)


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