package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var inputEmail: EditText
    lateinit var sendEmail: Button
    lateinit var memberRepository: MemberRepository
    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        inputEmail = findViewById(R.id.inputEmailForgotPassword)
        sendEmail = findViewById(R.id.btnForgotPassword)

        memberRepository = MemberRepository(application)

        sendEmail.setOnClickListener {
            if(inputEmail.text.toString().isNullOrEmpty()){
                Toast.makeText(this, "Preencha o e-mail", Toast.LENGTH_SHORT).show()
            } else {
                if(memberRepository.loadMembersByEmail(inputEmail.text.toString())) {
                    val secretQuestion = memberViewModel.getSecretQuestion(inputEmail.text.toString())
                    val intent = Intent(this, AnswerSecretQuestionActivity::class.java)
                    intent.putExtra("MEMBER_SECRET_QUESTION",secretQuestion)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "E-mail não cadastrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}