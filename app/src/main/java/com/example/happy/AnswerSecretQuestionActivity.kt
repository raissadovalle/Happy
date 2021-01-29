package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.happy.model.BillItem
import com.example.happy.model.Member
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel

class AnswerSecretQuestionActivity : AppCompatActivity() {

    lateinit var secretQuestion: TextView
    lateinit var secretAnswer: EditText
    lateinit var btnAnswerSecretQuestion: Button
    lateinit var member: Member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_secret_question)

        member = intent.getSerializableExtra("MEMBER_SECRET_QUESTION") as Member

        secretQuestion = findViewById(R.id.topText_secret_question)
        secretQuestion.text = member.secretQuestion.message
        secretAnswer = findViewById(R.id.inputAnswerSecretQuestion)
        btnAnswerSecretQuestion = findViewById(R.id.btnAnswerSecretQuestion)
        btnAnswerSecretQuestion.setOnClickListener {
            if(secretAnswer.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Preencha a resposta secreta!", Toast.LENGTH_SHORT).show()
            } else {
                if(secretAnswer.text.toString().toUpperCase().equals(member.secretAnswer.toUpperCase())) {
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra("ID_RESET_PASSWORD", member.id)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Resposta incorreta!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}