package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.happy.model.Rep
import com.example.happy.repository.MemberRepository
import com.example.happy.repository.RepRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.RepViewModel

class EnterExistingActivity : AppCompatActivity() {
    lateinit var enterRepCode: EditText
    lateinit var enterRepButton: Button
    lateinit var memberRepository: MemberRepository
    lateinit var repRepository: RepRepository
    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_existing)
        enterRepCode = findViewById(R.id.inputCode)
        enterRepButton = findViewById(R.id.btn_enter_existing_rep)
        memberRepository = MemberRepository(application)
        repRepository = RepRepository(application)
        enterRepButton.setOnClickListener {
            if (enterRepCode.text.any()) {
                val id = enterRepCode.text.toString()

                if(repRepository.loadRepByIdNoLiveData(id))
                {
                    memberViewModel.isLogged().observe(this, Observer{
                        it.repId = id;
                        memberViewModel.updateMember(it)
                        PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
                            if(id != null)
                                it.edit().putString(RepViewModel.REP_ID, id).apply()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.change()
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Preencha o campo, se não tem o código a república tem que adicionar você!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun Intent.change() {
        startActivity(this);
        finish();
    }
}