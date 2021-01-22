package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.happy.viewmodel.MemberViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var btnMemberLogin: Button
    lateinit var loginEmail: TextView
    lateinit var loginPassword: TextView

    private val memberViewModel by viewModels<MemberViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide();

        loginEmail = findViewById(R.id.inputEmail)
        loginPassword = findViewById(R.id.inputPassword)

        btnMemberLogin = findViewById(R.id.btnLogin)
        btnMemberLogin.setOnClickListener {
            memberViewModel.login(loginEmail.text.toString(), loginPassword.text.toString()).observe(this, Observer {
                if(it == null)
                    Toast.makeText(applicationContext, getString(R.string.login_message), Toast.LENGTH_SHORT).show()
                else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.change()
                }
            })
        }
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