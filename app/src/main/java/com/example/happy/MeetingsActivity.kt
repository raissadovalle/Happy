package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.MeetingAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.BillType
import com.example.happy.model.Meeting
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MeetingsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMeetings: RecyclerView

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

        val arrayMeetings = arrayListOf<Meeting>(
            Meeting(1, "12/01/2012", "Assunto 1","Pessoa falo X, fulano falou Y, siclano não concorda", "Decisão de achar um eletricista", "Fulano vai ligar para o eletricista"),
            Meeting(2, "05/01/2021", "Assunto 2","Pessoa falo X, fulano falou Y, siclano não concorda", "Não deixar a louça mais de 24 hrs na pia", "A louça sera separada por pessoa na pia"),
            Meeting(3, "03/01/2021", "Assunto 3","Pessoa falo X, fulano falou Y, siclano não concorda", "Avisar os moradores quando vierem visitas", "Avisar no grupo quando vierem pessoas"),
            Meeting(4, "01/01/2021", "Assunto 4","Pessoa falo X, fulano falou Y, siclano não concorda", "Sem som alto após a meia noite", "Sem encaminhamentos")
        )

        val adapterMeetings = MeetingAdapter(arrayMeetings, this)

        recyclerMeetings.adapter = adapterMeetings
        recyclerMeetings.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}