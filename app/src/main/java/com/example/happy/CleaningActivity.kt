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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.CleaningAdapter
import com.example.happy.model.*
import com.example.happy.repository.CleaningRepository
import com.example.happy.viewmodel.CleaningViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CleaningActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var buttonToolbar: Button
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerCleaningRooms: RecyclerView
    lateinit var adapterCleaning: CleaningAdapter
    private val cleaningViewModel by viewModels<CleaningViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()
    lateinit var memberLogged: Member

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning)

        toolbar = findViewById(R.id.toolbar_cleaning)
        setSupportActionBar(toolbar)

        buttonToolbar = findViewById(R.id.button_cleaning_tollbar)
        buttonToolbar.setOnClickListener {

            if(adapterCleaning.list.size > 0){
                
            } else {
                Toast.makeText(this, "Não há cômodos para atualizar!", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title_cleaning)
        textTitle.text = getString(R.string.cleaning_toolbar_title)

        floatingButton = findViewById(R.id.fb_cleaning)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddCleaningRoomActivity::class.java)
            intent.putExtra("IS_EDIT_CLEANING", false)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()

        recyclerCleaningRooms = findViewById(R.id.rv_cleaning)

        adapterCleaning = CleaningAdapter(this)

        memberViewModel.isLogged().observe(this, Observer {
            it?.let {
                memberLogged = it
                cleaningViewModel.getCleaningByRepId(it.repId!!).observe(this, Observer{ it2 ->
                    adapterCleaning.list = it2
                    adapterCleaning.notifyDataSetChanged()
                })
            }
        })

        recyclerCleaningRooms.adapter = adapterCleaning
        recyclerCleaningRooms.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val cleaningToRemove = adapterCleaning.list.removeAt(position)
                cleaningViewModel.deleteCleaning(cleaningToRemove)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = memberLogged.repId!!,
                    component = Notification.Components.CLEANINGS,
                    content = "O cômodo ${cleaningToRemove.desc} foi removido por ${memberLogged.name} em ${currentDate}",
                    date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("O cômodo ${cleaningToRemove.desc} foi removido por ${memberLogged.name} em ${currentDate}")
                adapterCleaning.notifyItemRemoved(position)
                adapterCleaning.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerCleaningRooms)
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