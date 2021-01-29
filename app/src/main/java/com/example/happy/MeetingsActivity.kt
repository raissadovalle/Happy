package com.example.happy

import android.app.LauncherActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.example.happy.model.Member
import com.example.happy.model.Notification
import com.example.happy.viewmodel.MeetingViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MeetingsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerMeetings: RecyclerView
    private val meetingViewModel by viewModels<MeetingViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()
    lateinit var memberLogged: Member

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
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
                memberLogged = it
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
                val meetingToRemove = adapterMeetings.list.removeAt(position)
                meetingViewModel.delete(meetingToRemove)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = memberLogged.repId!!,
                        component = Notification.Components.MEETINGS,
                        content = "A Reunião sobre ${meetingToRemove.subject} foi removida por ${memberLogged.name} em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("A Reunião sobre ${meetingToRemove.subject} foi removida por ${memberLogged.name} em ${currentDate}")
                adapterMeetings.notifyItemRemoved(position)
                adapterMeetings.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerMeetings)

    }

    fun sendNotification(description: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = android.app.Notification.Builder(this, channelId)
                .setContentTitle("Happy")
                .setContentText(description)
                .setSmallIcon(R .mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher_round))
                .setContentIntent(pendingIntent)

        notificationManager.notify(Math.random().toInt(), builder.build())
    }
}