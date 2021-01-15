package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.CleaningAdapter
import com.example.happy.model.*
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CleaningActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerCleaningRooms: RecyclerView

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

        val arrayCleaning = arrayListOf<Cleaning>(
            Cleaning(1, "Cozinha", Member(1, "José", "98653-5512", "jose@gmail.com", ProfilePhoto(1, "foto.com.br")), "12/01/2021", 15),
            Cleaning(2, "Banheiro", Member(2,"Raissa", "98653-5512", "raissa@gmail.com", ProfilePhoto(1, "foto.com.br")), "05/01/2021", 7),
            Cleaning(3, "Quarto 1", Member(3,"Bianca", "98653-5512", "bianca@gmail.com", ProfilePhoto(1, "foto.com.br")), "03/01/2021", 7),
            Cleaning(4, "Sala", Member(4,"Gabriela", "98653-5512", "amanda@gmail.com", ProfilePhoto(1, "foto.com.br")), "01/01/2021", 15)
        )

        val adapterCleaning = CleaningAdapter(arrayCleaning, this)

        recyclerCleaningRooms.adapter = adapterCleaning
        recyclerCleaningRooms.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}