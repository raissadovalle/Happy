package com.example.happy

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.happy.model.Member
import com.example.happy.viewmodel.MemberViewModel
import com.google.android.material.textfield.TextInputEditText

class MemberProfileActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var imageProfile: ImageView
    lateinit var photoURI: Uri
    lateinit var memberProfileFullName: TextInputEditText
    lateinit var memberProfileHometown: TextInputEditText
    lateinit var memberProfileCollege: TextInputEditText
    lateinit var memberProfileGraduate: TextInputEditText
    lateinit var memberProfileCelphone: TextInputEditText
    lateinit var memberProfileHometownState: Spinner
    lateinit var profileMember: Member
    lateinit var removeButtom: Button
    val REQUEST_TAKE_PHOTO = 1

    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.user_profile_toolbar_title)

        memberProfileFullName = findViewById(R.id.full_name)
        memberProfileHometown = findViewById(R.id.hometown)
        memberProfileCollege = findViewById(R.id.college)
        memberProfileGraduate = findViewById(R.id.graduate)
        memberProfileHometownState = findViewById(R.id.sp_state_member)
        memberProfileCelphone = findViewById(R.id.celNumber)
        removeButtom = findViewById(R.id.btn_remove_member)
        removeButtom.setOnClickListener {
            removeMember()
        }
        imageProfile = findViewById(R.id.iv_profile_member_image)

        profileMember = intent.getSerializableExtra("MEMBER") as Member

        memberProfileFullName.setText(profileMember.name)
        memberProfileHometown.setText(profileMember.hometown)
        memberProfileCollege.setText(profileMember.college)
        memberProfileGraduate.setText(profileMember.graduate)
        memberProfileCelphone.setText(profileMember.celNumber)
        //imageProfile.setImageURI(Uri.parse(it.image))
        resources.getStringArray(R.array.states).asList().indexOf(profileMember.hometownState).let {
            memberProfileHometownState.setSelection(it)
            memberProfileHometownState.setEnabled(false);
            memberProfileHometownState.setClickable(false)
        }
    }

    fun removeMember() {
        memberViewModel.isLogged().observe(this, Observer {
            profileMember.repId = ""
            memberViewModel.updateMember(profileMember)
            val intent = Intent(this, MembersActivity::class.java)
            intent.change()
        })
    }

    fun Intent.change()
    {
        startActivity(this)
        finish();
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}