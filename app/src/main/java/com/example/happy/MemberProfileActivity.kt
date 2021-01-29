package com.example.happy

import android.app.LauncherActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.happy.model.Member
import com.example.happy.model.Notification
import com.example.happy.repository.CleaningRepository
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class MemberProfileActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var imageProfile: ImageView
    lateinit var photoURI: Uri
    lateinit var memberProfileFullName: TextInputEditText
    lateinit var memberProfileHometown: TextInputEditText
    lateinit var memberProfileCollege: TextInputEditText
    lateinit var memberProfileGraduate: TextInputEditText
    lateinit var memberProfileCelphone: TextInputEditText
    lateinit var memberProfileHometownState: Spinner
    lateinit var profileMember: Member
    lateinit var removeButtom: Button
    val REQUEST_TAKE_PHOTO = 1

    private val notificationViewModel by viewModels<NotificationViewModel>()
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.user_profile_toolbar_title)

        memberProfileFullName = findViewById(R.id.full_name)
        memberProfileHometown = findViewById(R.id.hometown)
        memberProfileCollege = findViewById(R.id.college)
        memberProfileGraduate = findViewById(R.id.graduate)
        memberProfileHometownState = findViewById(R.id.sp_state_member)
        memberProfileCelphone = findViewById(R.id.celNumber)
        removeButtom = findViewById(R.id.btn_remove_member)
        removeButtom.setOnClickListener {
            removeMember()
        }
        imageProfile = findViewById(R.id.iv_profile_member_image)

        profileMember = intent.getSerializableExtra("MEMBER") as Member

        memberProfileFullName.setText(profileMember.name)
        memberProfileHometown.setText(profileMember.hometown)
        memberProfileCollege.setText(profileMember.college)
        memberProfileGraduate.setText(profileMember.graduate)
        memberProfileCelphone.setText(profileMember.celNumber)
        //imageProfile.setImageURI(Uri.parse(it.image))
        resources.getStringArray(R.array.states).asList().indexOf(profileMember.hometownState).let {
            memberProfileHometownState.setSelection(it)
            memberProfileHometownState.setEnabled(false);
            memberProfileHometownState.setClickable(false)
        }
    }

    fun removeMember() {
        var i = 0
        memberViewModel.isLogged().observe(this, Observer {
            if(i == 0) {
                i++
                profileMember.repId = ""
                memberViewModel.updateMember(profileMember)

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.MEMBERS,
                        content = "O membro ${profileMember.name} foi removido por ${it.name} em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("O membro ${profileMember.name} foi removido por ${it.name} em ${currentDate}")
                val intent = Intent(this, MembersActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.change()
            }
        })
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

    fun Intent.change()
    {
        startActivity(this)
        finish();
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}