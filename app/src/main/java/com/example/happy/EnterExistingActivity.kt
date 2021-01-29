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
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.happy.model.Notification
import com.example.happy.model.Rep
import com.example.happy.repository.MemberRepository
import com.example.happy.repository.RepRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.example.happy.viewmodel.RepViewModel
import java.text.SimpleDateFormat
import java.util.*

class EnterExistingActivity : AppCompatActivity() {
    lateinit var enterRepCode: EditText
    lateinit var enterRepButton: Button
    lateinit var memberRepository: MemberRepository
    lateinit var repRepository: RepRepository
    private val memberViewModel by viewModels<MemberViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_existing)
        enterRepCode = findViewById(R.id.inputCode)
        enterRepButton = findViewById(R.id.btn_enter_existing_rep)
        memberRepository = MemberRepository(application)
        repRepository = RepRepository(application)
        enterRepButton.setOnClickListener {
            if (enterRepCode.text.any()) {
                val id = enterRepCode.text.toString()

                if(repRepository.loadRepByIdNoLiveData(id))
                {
                    memberViewModel.isLogged().observe(this, Observer{
                        if(i == 0) {
                            it.repId = id;
                            memberViewModel.updateMember(it)
                            val sdf = SimpleDateFormat("dd/MM/yyyy")
                            val currentDate = sdf.format(Date())
                            val notification = Notification(memberId = id,
                                component = Notification.Components.MEMBERS,
                                content = "${it.name} entrou na sua República em ${currentDate}",
                                date = currentDate.toString() )
                            notificationViewModel.create(notification)
                            i++
                            sendNotification("${it.name} entrou na sua República em ${currentDate}")
                            PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
                                if(id.isNotEmpty())
                                    it.edit().putString(RepViewModel.REP_ID, id).apply()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.change()
                            }
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Preencha o campo, se não tem o código a república tem que adicionar você!", Toast.LENGTH_SHORT).show()
            }

        }

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

    fun Intent.change() {
        startActivity(this);
        finish();
    }
}