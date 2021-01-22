package com.example.happy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.example.happy.viewmodel.MemberViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {

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

    val REQUEST_TAKE_PHOTO = 1

    private val memberViewModel by viewModels<MemberViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.user_profile_toolbar_title)

        memberProfileFullName = findViewById(R.id.input_full_name)
        memberProfileHometown = findViewById(R.id.input_hometown)
        memberProfileCollege = findViewById(R.id.input_college)
        memberProfileGraduate = findViewById(R.id.input_graduate)
        memberProfileHometownState = findViewById(R.id.sp_state)
        memberProfileCelphone = findViewById(R.id.input_celNumber)

        imageProfile = findViewById(R.id.iv_profile_image)
        imageProfile.setOnClickListener{ takePicture() }

        val profileImage = PreferenceManager.getDefaultSharedPreferences(this).getString(MediaStore.EXTRA_OUTPUT, null)
        if(profileImage != null) {
            photoURI = Uri.parse(profileImage)
            imageProfile.setImageURI(photoURI)
        }
        else {
            imageProfile.setImageResource(R.mipmap.ic_launcher_round)
        }

        memberViewModel.isLogged().observe(this, Observer {
            if(it != null) {
                memberProfileFullName.setText(it.name)
                memberProfileHometown.setText(it.hometown)
                memberProfileCollege.setText(it.college)
                memberProfileGraduate.setText(it.graduate)
                memberProfileCelphone.setText(it.celNumber)
                resources.getStringArray(R.array.states).asList().indexOf(it.hometownState).let {
                    memberProfileHometownState.setSelection(it)
                }
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    @Throws(IOException::class)
    fun createImageFile() : File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "PROFILE ${timeStamp}",
                ".jpg",
                storageDir
        )
    }

    fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(this,
                    "com.example.happy.fileprovider",
                    it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            edit().putString(MediaStore.EXTRA_OUTPUT, photoURI.toString()).apply()

            imageProfile.setImageURI(photoURI)
        }
    }
}