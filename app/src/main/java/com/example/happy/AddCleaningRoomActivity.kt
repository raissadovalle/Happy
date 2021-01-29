package com.example.happy

import android.app.LauncherActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.happy.model.BillItem
import com.example.happy.model.Cleaning
import com.example.happy.model.Notification
import com.example.happy.model.Shopping
import com.example.happy.viewmodel.BillViewModel
import com.example.happy.viewmodel.CleaningViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.lang.Double
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AddCleaningRoomActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var descCleaning: TextInputEditText
    lateinit var chipGroupFrequency: ChipGroup
    lateinit var editCleaning: Cleaning
    lateinit var btnSaveCleaning: Button
    lateinit var btnCleanRoom: Button
    private val cleaningViewModel by viewModels<CleaningViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cleaning_room)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)

        chipGroupFrequency = findViewById(R.id.cg_add_cleaning)
        descCleaning = findViewById(R.id.add_cleaning_room_desc)
        btnSaveCleaning = findViewById(R.id.btn_save_cleaning_room)
        btnCleanRoom = findViewById(R.id.btn_clean_cleaning_room)

        var isEdit = intent.getSerializableExtra("IS_EDIT_CLEANING") as Boolean
        if(isEdit)
        {
            editCleaning = intent.getSerializableExtra("CLEANING") as Cleaning
            textTitle.text = getString(R.string.add_cleaning_room_app_bar_title_edit)
            descCleaning.setText(editCleaning.desc)
            btnSaveCleaning.setOnClickListener {
                editCleaning(editCleaning)
            }
            btnCleanRoom.setOnClickListener {
                cleanRoom()
            }
            if(editCleaning.wasCleaned){
                btnCleanRoom.setVisibility(View.GONE)
            }

            fillChipFrequency(editCleaning, isEdit)
        }
        else {
            btnCleanRoom.setVisibility(View.GONE)
            textTitle.text = getString(R.string.add_cleaning_room_app_bar_title_new)
            fillChipFrequency(Cleaning("", "", "", "","", "",false, Cleaning.Frequency.WEEKLY), isEdit)
            btnSaveCleaning.setOnClickListener {
                addCleaning()
            }
        }
    }

    private fun cleanRoom() {
        memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDate = sdf.format(Date())
            var cleaning = editCleaning
            cleaning.lastCleaned = currentDate.toString()
            cleaning.wasCleaned = true
            cleaningViewModel.updateCleaning(cleaning)
            val notification = Notification(memberId = it.repId!!,
                component = Notification.Components.CLEANINGS,
                content = "O cômodo ${cleaning.desc} foi limpo em ${currentDate} por ${it.name}",
                date = currentDate.toString())
            notificationViewModel.create(notification)
            sendNotification("O cômodo ${cleaning.desc} foi limpo em ${currentDate} por ${it.name}")
            val intent = Intent(this, CleaningActivity::class.java)
            intent.change()
        })
    }

    fun fillChipFrequency(editCleaning: Cleaning?, isEdit: Boolean) {
        val frequencys = Cleaning.Frequency.values()
        var i = 1;
        for (frequency in frequencys) {
            val chip = Chip(ContextThemeWrapper(chipGroupFrequency.context, R.style.Widget_MaterialComponents_Chip_Choice))
            chip.id = i
            chip.text = frequency.message
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_gray))
            chip.chipStrokeWidth = 1.0F
            chip.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)))
            chipGroupFrequency.addView(chip)
            if(isEdit)
            {
                if(chip.text == editCleaning!!.frequency.message)
                {
                    chip.performClick()
                }
            }
            i++
        }
    }

    fun addCleaning() {
        if(chipGroupFrequency.checkedChipId != View.NO_ID && descCleaning.text != null)
        {
            var chipValue = Cleaning.Frequency.WEEKLY;
            val chip = chipGroupFrequency.checkedChipId
            if(chip == 2) chipValue = Cleaning.Frequency.BIWEEKLY
            if(chip == 3) chipValue = Cleaning.Frequency.MONTLY

            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val cleaning = Cleaning(desc = descCleaning.text.toString(),
                    frequency = chipValue,
                    lastCleaned = "",
                    repId = it.repId!!,
                    memberId = "",
                    wasCleaned = false,
                    memberName = "")
                cleaningViewModel.createCleaning(cleaning)
                val notification = Notification(memberId = it.repId!!,
                    component = Notification.Components.CLEANINGS,
                    content = "O cômodo ${cleaning.desc} foi adicionado ao Quadro de Limpeza em ${currentDate} por ${it.name}",
                    date = currentDate.toString())
                notificationViewModel.create(notification)
                sendNotification("O cômodo ${cleaning.desc} foi adicionado ao Quadro de Limpeza em ${currentDate} por ${it.name}")
                val intent = Intent(this, CleaningActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }

    fun editCleaning(editCleaning: Cleaning) {
        if(chipGroupFrequency.checkedChipId != View.NO_ID && descCleaning.text.toString() != null)
        {
            var chipValue = Cleaning.Frequency.WEEKLY;
            val chip = chipGroupFrequency.checkedChipId
            if(chip == 2) chipValue = Cleaning.Frequency.BIWEEKLY
            if(chip == 3) chipValue = Cleaning.Frequency.MONTLY

            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val cleaning = Cleaning(id = editCleaning.id,
                    desc = descCleaning.text.toString(),
                    frequency = chipValue,
                    lastCleaned = editCleaning.lastCleaned,
                    repId = it.repId!!,
                    wasCleaned = false,
                    memberId = editCleaning.memberId,
                    memberName = editCleaning.memberName)
                cleaningViewModel.updateCleaning(cleaning)
                val notification = Notification(memberId = it.repId!!,
                    component = Notification.Components.CLEANINGS,
                    content = "O cômodo ${cleaning.desc} foi editado em ${currentDate} por ${it.name}",
                    date = currentDate.toString())
                notificationViewModel.create(notification)
                sendNotification("O cômodo ${cleaning.desc} foi editado em ${currentDate} por ${it.name}")
                val intent = Intent(this, CleaningActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    fun Intent.change()
    {
        startActivity(this)
        finish();
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