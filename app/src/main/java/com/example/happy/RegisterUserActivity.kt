package com.example.happy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_activity)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true;
    }
}