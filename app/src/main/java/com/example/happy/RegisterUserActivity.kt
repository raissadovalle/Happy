package com.example.happy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.happy.model.Member
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel

class RegisterUserActivity : AppCompatActivity() {

    lateinit var registerName: EditText
    lateinit var registerEmail: EditText
    lateinit var registerPassword: EditText
    lateinit var registerPassword2: EditText
    lateinit var registerButton: Button
    lateinit var memberRepository: MemberRepository
    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_activity)

        registerName = findViewById(R.id.inputName)
        registerEmail = findViewById(R.id.inputEmail)
        registerPassword = findViewById(R.id.inputPassword)
        registerPassword2 = findViewById(R.id.inputPasswordAgain)
        registerButton = findViewById(R.id.btnSaveUser)
        memberRepository = MemberRepository(application)
        registerButton.setOnClickListener{
            if(registerName.text.any() && registerEmail.text.any() && registerPassword.text.any() && registerPassword2.text.any())
            {
                if(registerPassword.text.toString() == registerPassword2.text.toString())
                {
                    val emailExistente = memberRepository.loadMembersByEmail(registerEmail.text.toString())
                    if(!emailExistente)
                    {
                        val member = Member(name = registerName.text.toString(),
                                email = registerEmail.text.toString(),
                                password = registerPassword.text.toString(),
                                repId = "",
                                graduate = "",
                                college = "",
                                hometown = "",
                                hometownState = "",
                                celNumber = ""
                                //image = ""
                        )
                        memberViewModel.createMember(member)
                        memberViewModel.login(member.email, member.password).observe(this, Observer {
                            val intent = Intent(this, AddNewRepOrEnterExistingActivity::class.java)
                            intent.change()
                        })
                    }
                    else
                    {
                        Toast.makeText(this,"E-mail já está sendo utilizado", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(this,"As senhas tem que ser iguais", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true;
    }

    fun Intent.change()
    {
        startActivity(this)
        finish();
    }
}