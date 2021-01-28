package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.MemberAdapter
import com.example.happy.model.Member
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.MemberViewModel.Companion.MEMBER_ID
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MembersActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMembers: RecyclerView
    val memberViewModel by viewModels<MemberViewModel>()
    lateinit var memberRepository: MemberRepository

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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()

        recyclerMembers = findViewById(R.id.rv_members)

        val adapterMembers = MemberAdapter(this)

        var memberId: String?
        var member : Member

        PreferenceManager.getDefaultSharedPreferences(application).let { memberId = it.getString(MEMBER_ID, null) }
        memberRepository = MemberRepository(application)
        member = memberRepository.loadMemberByIdNoLiveData(memberId!!)

        memberViewModel.getMemberByRepId(member.repId!!).observe(this, Observer{
            adapterMembers.list = it
            adapterMembers.notifyDataSetChanged()
        })

        recyclerMembers.adapter = adapterMembers
        recyclerMembers.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }
}