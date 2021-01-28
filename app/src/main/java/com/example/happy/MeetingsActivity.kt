package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.MeetingAdapter
import com.example.happy.viewmodel.MeetingViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MeetingsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMeetings: RecyclerView
    private val meetingViewModel by viewModels<MeetingViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()

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
            intent.putExtra("IS_EDIT_MEETING", false)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()

        recyclerMeetings = findViewById(R.id.rv_meetings)

        val adapterMeetings = MeetingAdapter(this)

        memberViewModel.isLogged().observe(this, Observer {
            it?.let {
                meetingViewModel.getMeetingsByRepId(it.repId!!).observe(this, Observer{ it2 ->
                    adapterMeetings.list = it2
                    adapterMeetings.notifyDataSetChanged()
                })
            }
        })

        recyclerMeetings.adapter = adapterMeetings
        recyclerMeetings.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val billToRemove = adapterMeetings.list.removeAt(position)
                meetingViewModel.delete(billToRemove)
                adapterMeetings.notifyItemRemoved(position)
                adapterMeetings.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerMeetings)

    }
}