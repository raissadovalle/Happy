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
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillCloseMonthAdapter
import com.example.happy.adapter.ShoppingAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.Notification
import com.example.happy.repository.BillRepository
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CloseMonthActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var recyclerBills: RecyclerView
    lateinit var billRepository: BillRepository
    lateinit var memberRepository: MemberRepository
    lateinit var totalRepLabel: TextView
    lateinit var totalRepPrice: TextView
    lateinit var totalMemberLabel: TextView
    lateinit var totalMemberPrice: TextView
    lateinit var confirmCloseMonth: Button
    lateinit var list: MutableList<BillItem>
    val memberViewModel by viewModels<MemberViewModel>()
    val billViewModel by viewModels<BillViewModel>()
    val repViewModel by viewModels<RepViewModel>()
    val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close_month)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.bills_close_month_toolbar_title)

        totalRepLabel = findViewById(R.id.total_rep_label)
        totalRepLabel.text = getString(R.string.total_rep_title)
        totalRepPrice = findViewById(R.id.total_rep_price)
        totalMemberLabel = findViewById(R.id.total_for_member_label)
        totalMemberLabel.text = getString(R.string.total_member_title)
        totalMemberPrice = findViewById(R.id.total_for_member_price)
        confirmCloseMonth = findViewById(R.id.btn_close_month_bill)
        confirmCloseMonth.setOnClickListener {
            confirm()
        }

        memberRepository = MemberRepository(application)
        billRepository = BillRepository(application)
    }

    private fun confirm() {
        memberViewModel.isLogged().observe(this, Observer {
            if(it != null)
            {
                for(i in list) {
                    billViewModel.updateCloseBill(i.id, true)
                }
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.BILLS,
                        content = "Mês fechado por ${it.name}, Total: ${totalRepPrice.text}, Divisão: ${totalMemberPrice.text} para cada em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("Mês fechado por ${it.name}, Total: ${totalRepPrice.text}, Divisão: ${totalMemberPrice.text} para cada em ${currentDate}")
                val intent = Intent(this, BillsActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, SplashActivity::class.java)
                memberViewModel.logout()
                repViewModel.removeRepId()
                startActivity(intent)
                finish()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()


        recyclerBills = findViewById(R.id.rv_close_month)

        val adapterBills = BillCloseMonthAdapter(this)


        memberViewModel.isLogged().observe(this, Observer {
            it?.let { it2 ->
                billRepository.loadBillsByRepIdAndIsClosed(it2.repId!!, true).let {
                    val countMembers = memberRepository.countMembersByRep(it2.repId!!)
                    adapterBills.list = it
                    list = it
                    adapterBills.notifyDataSetChanged()
                    var total = 0.00;
                    for(i in it) {
                        total += i.price;
                    }
                    val myPlace = Locale( "pt", "BR" )
                    val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
                    totalRepPrice.text= format.format(total)
                    totalMemberPrice.text= format.format(total/countMembers)
                }
            }
        })

        recyclerBills.adapter = adapterBills
        recyclerBills.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
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