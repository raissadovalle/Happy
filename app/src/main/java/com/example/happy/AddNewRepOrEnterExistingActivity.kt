package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel

class AddNewRepOrEnterExistingActivity : AppCompatActivity() {
    lateinit var btnAddRep: Button
    lateinit var btnEnterExistingRep: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_rep_or_enter_existing)

        btnAddRep = findViewById(R.id.btn_add_rep)
        btnAddRep.setOnClickListener {
            changeToNewRep()
        }
        btnEnterExistingRep = findViewById(R.id.btn_enter_existing_rep)
        btnEnterExistingRep.setOnClickListener {
            changeToEnterExistingRep()
        }
    }

    fun changeToNewRep() {
        val intent = Intent(this, RegisterRepActivity::class.java);

        intent.change();
    }

    fun changeToEnterExistingRep() {
        val intent = Intent(this, EnterExistingActivity::class.java)

        intent.change();
    }

    fun Intent.change() {
        startActivity(this);
        finish();
    }
}