package com.example.happy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.NotificationAdapter
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
import com.example.happy.viewmodel.RepViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var textTitle: TextView
    lateinit var textLogin: TextView
    lateinit var profilePicture: ImageView
    lateinit var recyclerNotifications: RecyclerView
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val memberViewModel by viewModels<MemberViewModel>()
    private val repViewModel by viewModels<RepViewModel>()

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
            R.id.nav_sign_out -> {
                memberViewModel.logout()
                repViewModel.removeRepId()
                finish()
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onResume() {
        super.onResume()

        val profileImage = PreferenceManager.getDefaultSharedPreferences(this).getString(MediaStore.EXTRA_OUTPUT, null)
//        if(profileImage != null) {
//            profilePicture.setImageURI(Uri.parse(profileImage))
//        }
//        else {
            profilePicture.setImageResource(R.mipmap.ic_launcher_round)
//        }

        recyclerNotifications = findViewById(R.id.rv_notifications)

        val adapterNotifications = NotificationAdapter(this)

        memberViewModel.isLogged().observe(this, Observer {
            it?.let {
                textLogin.text = it.name
                notificationViewModel.getNotificationByMemberId(it.repId!!).observe(this, Observer{ it2 ->
                    adapterNotifications.list = it2
                    adapterNotifications.notifyDataSetChanged()
                })
            }
        })

        recyclerNotifications.adapter = adapterNotifications
        recyclerNotifications.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }
}