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
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.CleaningViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    lateinit var memberRepository: MemberRepository
    lateinit var cleaningRepository: CleaningRepository
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning)

        toolbar = findViewById(R.id.toolbar_cleaning)
        setSupportActionBar(toolbar)

        memberRepository = MemberRepository(application)
        cleaningRepository = CleaningRepository(application)
        buttonToolbar = findViewById(R.id.button_cleaning_tollbar)
        buttonToolbar.setOnClickListener {

            if(adapterCleaning.list.size > 0){
                val listRemoveAllMembers = cleaningRepository.loadCleaningByRepNoLiveData(memberLogged.repId!!)



                val listIdMembers = takeMembersIds(adapterCleaning.list)
                var listCleanings: MutableList<Cleaning> = takeOfFrequency(adapterCleaning.list)
                var listMember: MutableList<Member> = memberRepository.loadMembersByRepIdNoLiveData(memberLogged.repId!!)
                var listDidntClean: MutableList<Member> = ArrayList()
                var listMembersFinal: MutableList<Member> = ArrayList()
                for(i in listMember) {
                    val finded = listIdMembers.find { r -> r == i.id }
                    if(!finded.isNullOrEmpty()) {
                        listMembersFinal.add(i)
                    }
                    else {
                        listDidntClean.add(i)
                    }
                }
                listDidntClean.sortBy { r-> r.id }
                listMembersFinal.sortBy{ r -> r.id }
                for(i in listDidntClean) {
                    listMembersFinal.add(0, i)
                }
                for(i in listRemoveAllMembers) {
                    i.memberId = ""
                    i.memberName = ""
                    cleaningViewModel.updateCleaning(i)
                }
                var j = 0;
                loop@ for(i in listCleanings) {
                    i.wasCleaned = false
                    i.memberId = listMembersFinal[j].id
                    i.memberName = listMembersFinal[j].name
                    cleaningViewModel.updateCleaning(i)
                    j++
                    if(listMembersFinal.size == j) break@loop
                }

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = memberLogged.repId!!,
                    component = Notification.Components.CLEANINGS,
                    content = "O Quadro de Limpeza foi atualizado por ${memberLogged.name} em ${currentDate}",
                    date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("O Quadro de Limpeza foi atualizado por ${memberLogged.name} em ${currentDate}")

                memberViewModel.isLogged().observe(this, Observer {
                    it?.let {
                        cleaningViewModel.getCleaningByRepId(it.repId!!).observe(this, Observer{ it2 ->
                            adapterCleaning.list = it2
                            adapterCleaning.notifyDataSetChanged()
                        })
                    }
                })

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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun takeMembersIds(list: MutableList<Cleaning>): MutableList<String> {
        var newList: MutableList<String> = ArrayList()
        for(i in list) {
            if(i.memberId!!.isNotEmpty()) {
                newList.add(i.memberId!!)
            }
        }
        if(newList.size == 0) return ArrayList()
        return newList.sorted().toMutableList()
    }

    fun takeOfFrequency(list: MutableList<Cleaning>) : MutableList<Cleaning> {

        var newList: MutableList<Cleaning> = ArrayList()

        for(i in list) {
            if(i.lastCleaned.isNullOrEmpty()) {
                newList.add(i)
            } else if(i.lastCleaned!!.isNotEmpty() && i.frequency == Cleaning.Frequency.WEEKLY) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                var date = LocalDate.parse(i.lastCleaned, formatter);
                date = date.plusDays(7)
                var dateCompareAfter = (LocalDate.now().with(DayOfWeek.MONDAY)).minusDays(1)
                var dateCompareBefore = (LocalDate.now().with(DayOfWeek.SUNDAY)).plusDays(1)
                if(date.isAfter(dateCompareAfter) && date.isBefore(dateCompareBefore)){
                    newList.add(i)
                }
            } else if(i.lastCleaned!!.isNotEmpty() && i.frequency == Cleaning.Frequency.BIWEEKLY) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                var date = LocalDate.parse(i.lastCleaned, formatter);
                date = date.plusDays(15)
                var dateCompareAfter = (LocalDate.now().with(DayOfWeek.MONDAY)).minusDays(1)
                var dateCompareBefore = (LocalDate.now().with(DayOfWeek.SUNDAY)).plusDays(1)
                if(date.isAfter(dateCompareAfter) && date.isBefore(dateCompareBefore)){
                    newList.add(i)
                }
            } else if(i.lastCleaned!!.isNotEmpty() && i.frequency == Cleaning.Frequency.MONTLY) {
                var date = LocalDate.parse(i.lastCleaned, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                date = date.plusDays(30)
                var dateCompareAfter = (LocalDate.now().with(DayOfWeek.MONDAY)).minusDays(1)
                var dateCompareBefore = (LocalDate.now().with(DayOfWeek.SUNDAY)).plusDays(1)
                if(date.isAfter(dateCompareAfter) && date.isBefore(dateCompareBefore)){
                    newList.add(i)
                }
            }
        }

        if(newList.size == 0) return ArrayList()
        return newList.shuffled() as MutableList<Cleaning>
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