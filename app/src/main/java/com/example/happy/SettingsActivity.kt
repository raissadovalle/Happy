package com.example.happy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.MeetingAdapter
import com.example.happy.model.Meeting
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsActivity: AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.settings_toolbar_title)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}