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
import com.example.happy.model.BillMonths
import com.example.happy.model.Member
import com.example.happy.model.Notification
import com.example.happy.viewmodel.BillViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.menu_toolbar_layout.*
import java.lang.Double.parseDouble
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddEditBillActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var descBill: TextInputEditText
    lateinit var priceBill: TextInputEditText
    lateinit var chipGroupTypes: ChipGroup
    lateinit var btnSaveBill: Button
    val billViewModel by viewModels<BillViewModel>()
    val memberViewModel by viewModels<MemberViewModel>()
    val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_bill)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)

        chipGroupTypes = findViewById(R.id.cg_add_bill)
        descBill = findViewById(R.id.add_bill_desc)
        priceBill = findViewById(R.id.add_bill_price)
        btnSaveBill = findViewById(R.id.btn_save_bill)

        var isEdit = intent.getSerializableExtra("IS_EDIT_BILL") as Boolean
        if(isEdit)
        {
            var editBill = intent.getSerializableExtra("BILL") as BillItem
            textTitle.text = getString(R.string.add_bill_app_bar_title_edit)
            descBill.setText(editBill.desc)
            priceBill.setText(editBill.price.toString())
            btnSaveBill.setOnClickListener {
                editBill(editBill)
            }
            fillChipTypes(editBill, isEdit)
        }
        else {
            textTitle.text = getString(R.string.add_bill_app_bar_title_new)
            fillChipTypes(BillItem("", "", "", 0.00, BillItem.BillType.OTHERS,"", false), isEdit)
            btnSaveBill.setOnClickListener {
                addBill()
            }
        }
    }

    fun fillChipTypes(editBill: BillItem?, isEdit: Boolean) {
        val types = BillItem.BillType.values()
        var i = 1;
        for (type in types) {
            val chip = Chip(ContextThemeWrapper(chipGroupTypes.context, R.style.Widget_MaterialComponents_Chip_Choice))
            chip.id = i
            chip.text = type.message
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_gray))
            chip.chipStrokeWidth = 1.0F
            chip.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)))
            chipGroupTypes.addView(chip)
            if(isEdit)
            {
                if(chip.text == editBill!!.type.message)
                {
                    chip.performClick()
                }
            }
            i++
        }
    }

    fun addBill() {
        if(chipGroupTypes.checkedChipId != View.NO_ID && descBill.text != null && priceBill.text != null)
        {
            var chipValue = BillItem.BillType.OTHERS;
            val chip = chipGroupTypes.checkedChipId
            if(chip == 1) chipValue = BillItem.BillType.RENT
            if(chip == 3) chipValue = BillItem.BillType.WATER
            if(chip == 2) chipValue = BillItem.BillType.ELECTRICITY
            if(chip == 4) chipValue = BillItem.BillType.SHOPPING

            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val bill = BillItem(desc = descBill.text.toString(),
                        price = parseDouble(priceBill.text.toString()),
                        type = chipValue,
                        date = currentDate.toString(),
                        repId = it.repId!!,
                        isClosed = false)
                billViewModel.createBill(bill)
                val myPlace = Locale( "pt", "BR" )
                val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.BILLS,
                        content = "Conta ${bill.desc} de ${format.format(bill.price)} adicionada às Contas em ${currentDate} por ${it.name}",
                        date = currentDate.toString())
                notificationViewModel.create(notification)
                sendNotification("Conta ${bill.desc} de ${format.format(bill.price)} adicionada às Contas em ${currentDate} por ${it.name}")
                val intent = Intent(this, BillsActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }

    fun editBill(editBill: BillItem) {
        if(chipGroupTypes.checkedChipId != View.NO_ID && descBill.text != null && priceBill.text != null)
        {
            var chipValue = BillItem.BillType.OTHERS;
            val chip = chipGroupTypes.checkedChipId
            if(chip == 1) chipValue = BillItem.BillType.RENT
            if(chip == 2) chipValue = BillItem.BillType.WATER
            if(chip == 3) chipValue = BillItem.BillType.ELECTRICITY
            if(chip == 4) chipValue = BillItem.BillType.SHOPPING

            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val bill = BillItem(id = editBill.id,
                        desc = descBill.text.toString(),
                        price = parseDouble(priceBill.text.toString()),
                        type = chipValue,
                        date = editBill.date,
                        repId = it.repId!!,
                        isClosed = false)
                billViewModel.updateBill(bill)
                val myPlace = Locale( "pt", "BR" )
                val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.BILLS,
                        content = "Conta ${bill.desc} de ${format.format(bill.price)} foi editada em ${currentDate} por ${it.name}",
                        date = currentDate.toString())
                notificationViewModel.create(notification)
                sendNotification("Conta ${bill.desc} de ${bill.price} foi editada em ${currentDate} por ${it.name}")
                val intent = Intent(this, BillsActivity::class.java)
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