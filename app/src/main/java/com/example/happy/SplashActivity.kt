package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.happy.viewmodel.MemberViewModel

class SplashActivity : AppCompatActivity() {

    private val memberViewModel by viewModels<MemberViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide();

        memberViewModel.isLogged().observe(this, Observer {
            if(it != null) {
                changeToMenu()
            }
            else {
                changeToLogin()
            }
        })
    }

    fun changeToLogin() {
        val intent = Intent(this, LoginActivity::class.java);

        Handler().postDelayed(
            {
                intent.change();
            }, 2000
        )
    }

    fun changeToMenu() {
        val intent = Intent(this, MainActivity::class.java)

        Handler().postDelayed(
            {
                intent.change();
            }, 2000
        )
    }

    fun Intent.change() {
        startActivity(this);
        finish();
    }
}