package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar()?.hide();
    }

    fun changeToForgotPassword(view: View)
    {
        val intent = Intent(this, ForgotPasswordActivity::class.java)

        intent.change();
    }

    fun changeToRegister(view: View)
    {
        val intent = Intent(this, RegisterUserActivity::class.java)

        intent.change();
    }

    fun loginOrRegisterWithGoogle()
    {

    }

    fun loginOrRegisterWithFacebook()
    {

    }

    fun Intent.change()
    {
        startActivity(this)
        finish();
    }
}