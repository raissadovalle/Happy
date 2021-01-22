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
import com.example.happy.adapter.MemberAdapter
import com.example.happy.viewmodel.MemberViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MembersActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMembers: RecyclerView
    val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.members_toolbar_title)

        floatingButton = findViewById(R.id.fb_members)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddNewMemberActivity::class.java)
            startActivity(intent)
        }

        recyclerMembers = findViewById(R.id.rv_members)

        val adapterMembers = MemberAdapter(this)

        memberViewModel.getMemberByRepId("1").observe(this, Observer{
            adapterMembers.list = it
            adapterMembers.notifyDataSetChanged()
        }) //TODO repId

        recyclerMembers.adapter = adapterMembers
        recyclerMembers.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}