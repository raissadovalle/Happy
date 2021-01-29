package com.example.happy

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.BillMonths
import com.example.happy.model.Member
import com.example.happy.model.Notification
import com.example.happy.repository.BillRepository
import com.example.happy.viewmodel.BillViewModel
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.example.happy.viewmodel.RepViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Integer.parseInt
import java.lang.Math.random
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class BillsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var buttonToolbar: Button
    lateinit var textTitle: TextView
    lateinit var chipGroupMonths: ChipGroup
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerBills: RecyclerView
    lateinit var adapterBills: BillItemAdapter
    private val billViewModel by viewModels<BillViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()
    lateinit var memberLogged: Member

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills)

        toolbar = findViewById(R.id.toolbar_bill)
        setSupportActionBar(toolbar)
        buttonToolbar = findViewById(R.id.button_bill_tollbar)
        buttonToolbar.setOnClickListener {

            if(adapterBills.list.filter{ it.isClosed == false}.size > 0){
                val intent = Intent(this, CloseMonthActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Não há contas em aberto!", Toast.LENGTH_SHORT).show()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title_bill)
        textTitle.text = getString(R.string.bills_toolbar_title)

        floatingButton = findViewById(R.id.fb_bills)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddEditBillActivity::class.java)
            intent.putExtra("IS_EDIT_BILL", false)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun fillChipMonths(list: List<BillItem>, adapterBills: BillItemAdapter) {
        chipGroupMonths.removeAllViews()
        var months: MutableList<BillMonths> = ArrayList()
        for(i in list) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            val date = LocalDate.parse(i.date, formatter);
            val month = BillMonths(date.monthValue.toString(), date.year.toString())
            months.add(month)
        }
        var monthsList = months.toList().distinct()
        for (month in monthsList) {
            val chip = Chip(
                    ContextThemeWrapper(
                            chipGroupMonths.context,
                            R.style.Widget_MaterialComponents_Chip_Choice
                    )
            )
            chip.text = month.month + "/" + month.year
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                            this,
                            R.color.dark_gray
                    )
            )
            chip.chipStrokeWidth = 1.0F
            chip.chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                            this,
                            R.color.dark_green
                    )
            )
            chip.setTextColor(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    this,
                                    R.color.white
                            )
                    )
            )
            chip.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    val text = (view as Chip).text.toString()
                    memberViewModel.isLogged().observe(this@BillsActivity, Observer {
                        it?.let {
                            billViewModel.getBillByRepId(it.repId!!).observe(this@BillsActivity, Observer { it2 ->
                                adapterBills.list = takeOutOtherMonths(it2, text)
                                adapterBills.notifyDataSetChanged()
                            })
                        }
                    })
                }
            })
            chipGroupMonths.addView(chip)
            if(monthsList.indexOf(month) == monthsList.size-1)
            {
                chip.performClick()
            }
        }
    }

    fun takeOutOtherMonths(list: MutableList<BillItem>, monthYear: String) : MutableList<BillItem>{
        var list2 = list.toMutableList()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(i in list) {

            val date = LocalDate.parse(i.date, formatter);

            if(date.monthValue != parseInt(monthYear.substringBefore("/")) || date.year != parseInt(monthYear.substringAfter("/"))){
                list2.remove(i)
            }
        }
        var sortedList: List<BillItem>
        sortedList = list2.sortedBy { r -> r.date }
        return sortedList.toMutableList()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()

        recyclerBills = findViewById(R.id.rv_bills)

        adapterBills = BillItemAdapter(this)

        recyclerBills.adapter = adapterBills
        recyclerBills.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        memberViewModel.isLogged().observe(this, Observer {
            it?.let {
                memberLogged = it
                billViewModel.getBillByRepId(it.repId!!).observe(this, Observer { it2 ->
                    adapterBills.list = it2
                    adapterBills.notifyDataSetChanged()

                    chipGroupMonths = findViewById(R.id.chip_group_months)
                    fillChipMonths(it2, adapterBills)

                })
            }
        })

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val billToRemove = adapterBills.list.removeAt(position)
                billViewModel.deleteBill(billToRemove)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val myPlace = Locale( "pt", "BR" )
                val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
                val notification = Notification(memberId = memberLogged.repId!!,
                        component = Notification.Components.BILLS,
                        content = "A Conta ${billToRemove.desc} de ${format.format(billToRemove.price)} foi removido por ${memberLogged.name} em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("A Conta ${billToRemove.desc} de ${format.format(billToRemove.price)} foi removido por ${memberLogged.name} em ${currentDate}")
                adapterBills.notifyItemRemoved(position)
                adapterBills.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerBills)
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