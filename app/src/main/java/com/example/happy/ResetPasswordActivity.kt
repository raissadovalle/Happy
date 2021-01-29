package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.happy.model.Member
import com.example.happy.viewmodel.MemberViewModel

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var resetPassword: EditText
    lateinit var resetPassword2: EditText
    lateinit var btnResetPassword: Button
    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val id = intent.getSerializableExtra("ID_RESET_PASSWORD") as String

        resetPassword = findViewById(R.id.inputResetPassword)
        resetPassword2 = findViewById(R.id.inputResetPasswordAgain)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        btnResetPassword.setOnClickListener {
            if(resetPassword.text.toString().isNullOrEmpty() && resetPassword2.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Preencha os campos!", Toast.LENGTH_SHORT).show()
            } else {
                if(resetPassword.text.toString().equals(resetPassword2.text.toString())) {
                    memberViewModel.updatePassword(id, resetPassword.text.toString())
                    Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "As senhas tem que ser iguais!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}