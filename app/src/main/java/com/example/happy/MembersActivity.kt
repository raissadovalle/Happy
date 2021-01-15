package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.MemberAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.BillType
import com.example.happy.model.Member
import com.example.happy.model.ProfilePhoto
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.menu_toolbar_layout.*

class MembersActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMembers: RecyclerView

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

        val arrayMembers = arrayListOf<Member>(
            Member(1, "Raissa", "98325-5521", "dovalleraissa@gmail.com", ProfilePhoto(1, "www.photo.com")),
            Member(2, "Bianca", "98654-6598", "bicotarelli@gmail.com", ProfilePhoto(1, "www.photo.com")),
            Member(3, "José", "66546-5644", "jrubesoliveira@hotmail.com", ProfilePhoto(1, "www.photo.com")),
            Member(4, "Amanda", "65548-9977", "amanda_portela@terra.com", ProfilePhoto(1, "www.photo.com"))
        )

        val adapterMembers = MemberAdapter(arrayMembers, this)

        recyclerMembers.adapter = adapterMembers
        recyclerMembers.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}