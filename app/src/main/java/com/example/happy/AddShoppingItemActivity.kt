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
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.example.happy.model.BillItem
import com.example.happy.model.Meeting
import com.example.happy.model.Notification
import com.example.happy.model.Shopping
import com.example.happy.viewmodel.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Double
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AddShoppingItemActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var btnSaveShoppingItem: Button
    lateinit var btnBuyShoppingItem: Button
    lateinit var editShoppingItem: Shopping
    lateinit var desc: TextInputEditText
    lateinit var price: TextInputEditText
    lateinit var descTitle: TextInputLayout
    lateinit var priceTitle: TextInputLayout
    val shoppingViewModel by viewModels<ShoppingViewModel>()
    val memberViewModel by viewModels<MemberViewModel>()
    val billViewModel by viewModels<BillViewModel>()
    val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isEdit = intent.getSerializableExtra("IS_EDIT_SHOPPING") as Boolean
        if(isEdit)setContentView(R.layout.activity_buy_shopping_item)
        else setContentView(R.layout.activity_add_shopping_item)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)

        desc = findViewById(R.id.add_shopping_item_desc)
        if(isEdit) price = findViewById(R.id.add_shopping_item_price)
        descTitle = findViewById(R.id.add_shopping_item_desc_title)
        if(isEdit) priceTitle = findViewById(R.id.add_shopping_item_price_title)
        btnSaveShoppingItem = findViewById(R.id.btn_add_shopping_item)
        btnBuyShoppingItem = findViewById(R.id.btn_buy_shopping_item)


        if(isEdit)
        {
            editShoppingItem = intent.getSerializableExtra("SHOPPING") as Shopping
            textTitle.text = getString(R.string.buy_meeting_app_bar_title_edit)
            desc.setText(editShoppingItem.desc)
            desc.setClickable(false)
            desc.setEnabled(false)
            btnSaveShoppingItem.setVisibility(View.GONE)
            btnBuyShoppingItem.setOnClickListener {
                buyShoppingItem(editShoppingItem)
            }
        }
        else {
            textTitle.text = getString(R.string.add_shopping_item_app_bar_title)
            btnBuyShoppingItem.setVisibility(View.GONE)
            btnSaveShoppingItem.setOnClickListener {
                addShoppingItem()
            }
        }

    }

    fun addShoppingItem() {
        if(desc.text.toString() != null)
        {
            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val shopping = Shopping(repId = it.repId!!,
                        desc = desc.text.toString(),
                        price = null,
                        date = currentDate.toString())
                shoppingViewModel.create(shopping)
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.SHOPPINGS,
                        content = "Item ${shopping.desc} adicionado às Compras em ${currentDate} por ${it.name}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("Item ${shopping.desc} adicionado às Compras em ${currentDate} por ${it.name}")
                val intent = Intent(this, ShoppingActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"A descrição é obrigatória!", Toast.LENGTH_SHORT).show()
        }
    }

    fun buyShoppingItem(editShopping: Shopping) {
        if(price.text.toString() != null)
        {
            memberViewModel.isLogged().observe(this, androidx.lifecycle.Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val bill = BillItem(
                        repId = it.repId!!,
                        desc = desc.text.toString(),
                        type = BillItem.BillType.SHOPPING,
                        price = Double.parseDouble(price.text.toString()),
                        date = currentDate.toString(),
                        isClosed = false)
                val myPlace = Locale( "pt", "BR" )
                val format: NumberFormat = NumberFormat.getCurrencyInstance(myPlace)
                val notification = Notification(memberId = it.repId!!,
                        component = Notification.Components.SHOPPINGS,
                        content = "Item ${format.format(bill.desc)} foi comprado por ${it.name} e adicionado as Contas em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("Item ${format.format(bill.desc)} foi comprado por ${it.name} e adicionado as Contas em ${currentDate}")
                shoppingViewModel.delete(editShopping)
                billViewModel.createBill(bill)
                val intent = Intent(this, ShoppingActivity::class.java)
                intent.change()
            })
        }
        else
        {
            Toast.makeText(this,"Preencha o preço!", Toast.LENGTH_SHORT).show()
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