package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView

class MyRepActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var billsCardView: CardView
    lateinit var cleaningCardView: CardView
    lateinit var membersCardView: CardView
    lateinit var meetingsCardView: CardView
    lateinit var shoppingCardView: CardView
    lateinit var buttonToolbar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rep)

        toolbar = findViewById(R.id.toolbar_my_rep)
        setSupportActionBar(toolbar)
        buttonToolbar = findViewById(R.id.iv_my_rep)
        buttonToolbar.setOnClickListener {
            val intent = Intent(this, RepProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title_my_rep)
        textTitle.text = "Minha República"

        billsCardView = findViewById(R.id.cv_bills)
        billsCardView.setOnClickListener {
            val intent = Intent(this, BillsActivity::class.java)
            startActivity(intent)
        }

        cleaningCardView = findViewById(R.id.cv_cleaning)
        cleaningCardView.setOnClickListener {
            val intent = Intent(this, CleaningActivity::class.java)
            startActivity(intent)
        }

        membersCardView = findViewById(R.id.cv_members)
        membersCardView.setOnClickListener {
            val intent = Intent(this, MembersActivity::class.java)
            startActivity(intent)
        }

        meetingsCardView = findViewById(R.id.cv_meetings)
        meetingsCardView.setOnClickListener {
            val intent = Intent(this, MeetingsActivity::class.java)
            startActivity(intent)
        }

        shoppingCardView = findViewById(R.id.cv_shopping)
        shoppingCardView.setOnClickListener {
            val intent = Intent(this, ShoppingActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

}