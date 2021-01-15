package com.example.happy

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.R
import com.example.happy.adapter.MeetingAdapter
import com.example.happy.adapter.NotificationAdapter
import com.example.happy.model.Components
import com.example.happy.model.Meeting
import com.example.happy.model.Notification
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var textTitle: TextView
    lateinit var textLogin: TextView
    lateinit var profilePicture: ImageView
    lateinit var recyclerNotifications: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.app_name)

        drawerLayout = findViewById(R.id.nav_drawer_layout)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.toggle_open, R.string.toggle_close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        textLogin = navigationView.getHeaderView(0).findViewById(R.id.header_profile_name)
        textLogin.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        profilePicture = navigationView.getHeaderView(0).findViewById(R.id.header_profile_image)
        profilePicture.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        recyclerNotifications = findViewById(R.id.rv_notifications)

        val arrayNotifications = arrayListOf<Notification>(
            Notification(1, "Foi comprado: Amaciante R$ 10,99 por Raissa", Components(4, "Compras"),"12/01/2012"),
            Notification(2, "O Banheiro foi limpo por José", Components(2, "Limpeza"),"12/01/2012"),
            Notification(3, "Novo membro Leonardo foi adicionado", Components(3, "Membros"),"12/01/2012"),
            Notification(4, "Conta de Àgua adicionada de R$ 119,80", Components(1, "Contas"),"12/01/2012")
        )

        val adapterNotifications = NotificationAdapter(arrayNotifications, this)

        recyclerNotifications.adapter = adapterNotifications
        recyclerNotifications.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_my_rep -> {
                val intent = Intent(this, MyRepActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_ads -> {
                val intent = Intent(this, AdsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_help -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
            }
//            R.id.nav_sign_out -> {
//                val intent = Intent(this, SignOutActivity::class.java)
//                startActivity(intent)
//            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}