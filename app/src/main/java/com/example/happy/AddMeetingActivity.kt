package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.example.happy.model.BillItem
import com.example.happy.model.Meeting
import com.example.happy.viewmodel.BillViewModel
import com.example.happy.viewmodel.MeetingViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.google.android.material.textfield.TextInputEditText
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*

class AddMeetingActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var btnSaveMeeting: Button
    lateinit var editMeeting: Meeting
    lateinit var subject: TextInputEditText
    lateinit var forwarding: TextInputEditText
    lateinit var debate: TextInputEditText
    lateinit var verdict: TextInputEditText
    val meetingViewModel by viewModels<MeetingViewModel>()
    val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)

        subject = findViewById(R.id.add_meeting_subject)
        debate = findViewById(R.id.add_meeting_debate)
        verdict = findViewById(R.id.add_meeting_verdict)
        forwarding = findViewById(R.id.add_meeting_forwarding)
        btnSaveMeeting = findViewById(R.id.btn_save_meeting)


        var isEdit = intent.getSerializableExtra("IS_EDIT_MEETING") as Boolean
        if(isEdit)
        {
            editMeeting = intent.getSerializableExtra("MEETING") as Meeting
            textTitle.text = getString(R.string.add_meeting_app_bar_title_edit)
            subject.setText(editMeeting.subject)
            debate.setText(editMeeting.debate)
            verdict.setText(editMeeting.verdict)
            forwarding.setText(editMeeting.forwarding)
            btnSaveMeeting.setOnClickListener {
                editMeeting(editMeeting)
            }
        }
        else {
            textTitle.text = getString(R.string.add_meeting_app_bar_title_new)
            btnSaveMeeting.setOnClickListener {
                addMeeting()
            }
        }
    }

    fun addMeeting() {
        if(subject.text != null && verdict.text != null)
        {
            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val meeting = Meeting(repId = it.repId!!,
                    subject = subject.text.toString(),
                    debate = debate.text.toString(),
                    verdict = verdict.text.toString(),
                    date = currentDate.toString(),
                    forwarding = forwarding.text.toString())
                meetingViewModel.create(meeting)
                val intent = Intent(this, MeetingsActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"O assunto e a decisão são obrigatórios!", Toast.LENGTH_SHORT).show()
        }
    }

    fun editMeeting(editMeeting: Meeting) {
        if(subject.text != null && verdict.text != null)
        {
            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val meeting = Meeting( id = editMeeting.id,
                    repId = it.repId!!,
                    subject = subject.text.toString(),
                    debate = debate.text.toString(),
                    verdict = verdict.text.toString(),
                    date = editMeeting.date,
                    forwarding = forwarding.text.toString())
                meetingViewModel.update(meeting)
                val intent = Intent(this, MeetingsActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"O assunto e a decisão são obrigatórios!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    fun Intent.change()
    {
        startActivity(this)
        finish();
    }
}