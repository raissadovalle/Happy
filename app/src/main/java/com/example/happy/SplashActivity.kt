package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    var isLogged: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide();

        if(isLogged) {
            changeToMenu();
        }
        else {
            changeToLogin();
        }
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