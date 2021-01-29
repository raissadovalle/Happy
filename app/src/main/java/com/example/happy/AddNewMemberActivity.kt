package com.example.happy

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.RepViewModel
import kotlinx.android.synthetic.main.activity_add_new_member.*

class AddNewMemberActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var emailNewMember: TextView
    lateinit var buttonSendEmail: Button
    private val memberViewModel by viewModels<MemberViewModel>()
    private val repViewModel by viewModels<RepViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_member)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.add_member_app_bar_title)

        emailNewMember = findViewById(R.id.add_member_email)
        buttonSendEmail = findViewById(R.id.btn_send_invite)
        buttonSendEmail.setOnClickListener { sendEmail() }
    }

    fun sendEmail() {
        if(emailNewMember.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show()
        } else {
            memberViewModel.isLogged().observe(this, Observer {
                repViewModel.getRepById(it.repId!!).observe(this, Observer { it2 ->
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setType("message/rfc822")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Convite para entrar na República ${it2.name}")
                    intent.putExtra(Intent.EXTRA_TEXT, "Para entrar na República ${it2.name}, faça o seu cadastro no app Happy, clique em entrar em república e digite o código : ${it2.id} \n\nEsperamos você lá! ")
                    intent.setData(Uri.parse("mailto:${emailNewMember.text}"))

                    try {
                        startActivity(Intent.createChooser(intent, "Enviar email por..."))
//                        val i = Intent(this, MembersActivity::class.java)
//                        startActivity(i)
//                        finish()
                    } catch (ex : ActivityNotFoundException) {
                        Toast.makeText(this@AddNewMemberActivity, "Não tem nenhum app de e-mail instalado!", Toast.LENGTH_SHORT).show()
                        val i = Intent(this, MembersActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                        finish()
                    }
                })
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}