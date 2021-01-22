package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.CleaningAdapter
import com.example.happy.model.*
import com.example.happy.repository.CleaningRepository
import com.example.happy.viewmodel.CleaningViewModel
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CleaningActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerCleaningRooms: RecyclerView
    private val cleaningViewModel by viewModels<CleaningViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.cleaning_toolbar_title)

        floatingButton = findViewById(R.id.fb_cleaning)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddCleaningRoomActivity::class.java)
            startActivity(intent)
        }

        recyclerCleaningRooms = findViewById(R.id.rv_cleaning)

        val adapterCleaning = CleaningAdapter(this)

        cleaningViewModel.getCleaningByRepId("1").observe(this, Observer{
            adapterCleaning.list = it
            adapterCleaning.notifyDataSetChanged()
        }) //TODO repid

        recyclerCleaningRooms.adapter = adapterCleaning
        recyclerCleaningRooms.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}