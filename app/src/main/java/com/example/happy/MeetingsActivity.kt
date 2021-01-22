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
import com.example.happy.adapter.MeetingAdapter
import com.example.happy.viewmodel.MeetingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MeetingsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMeetings: RecyclerView
    private val meetingviewModel by viewModels<MeetingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetings)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.meetings_toolbar_title)

        floatingButton = findViewById(R.id.fb_meetings)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddMeetingActivity::class.java)
            startActivity(intent)
        }

        recyclerMeetings = findViewById(R.id.rv_meetings)

        val adapterMeetings = MeetingAdapter(this)

        meetingviewModel.getMeetingsByRepId("1").observe(this, Observer{
            adapterMeetings.list = it
            adapterMeetings.notifyDataSetChanged()
        }) //TODO repid

        recyclerMeetings.adapter = adapterMeetings
        recyclerMeetings.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}