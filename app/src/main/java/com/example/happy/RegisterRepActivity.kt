package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.happy.model.Member
import com.example.happy.model.Rep
import com.example.happy.repository.MemberRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.RepViewModel
import androidx.lifecycle.Observer as Observer

class RegisterRepActivity : AppCompatActivity() {
    lateinit var registerRepName: EditText
    lateinit var registerAddress: EditText
    lateinit var registerRepButton: Button
    lateinit var memberRepository: MemberRepository
    private val memberViewModel by viewModels<MemberViewModel>()
    private val repViewModel by viewModels<RepViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_rep)

        registerRepName = findViewById(R.id.inputNameRep)
        registerAddress = findViewById(R.id.inputEnd)
        registerRepButton = findViewById(R.id.btnSaveRep)
        memberRepository = MemberRepository(application)
        registerRepButton.setOnClickListener {
            if (registerRepName.text.any() && registerAddress.text.any()) {
            val rep = Rep(
                name = registerRepName.text.toString(),
                address = registerAddress.text.toString()
            )
                repViewModel.create(rep)
                repViewModel.saveRepId(rep.name, rep.address!!).observe(this, Observer { it3 ->
                    memberViewModel.isLogged().observe(this, Observer { it2 ->
                        it2.repId = it3.id
                        memberViewModel.enterRep(it2)
                    })
                    val intent = Intent(this, MainActivity::class.java)
                    intent.change()
                })
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun Intent.change() {
        startActivity(this);
        finish();
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true;
    }
}