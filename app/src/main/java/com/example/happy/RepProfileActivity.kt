package com.example.happy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.happy.model.Member
import com.example.happy.model.Rep
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.RepViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RepProfileActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var imageProfile: ImageView
    lateinit var photoURI: Uri
    lateinit var repProfileName: TextInputEditText
    lateinit var repProfileAddress: TextInputEditText
    lateinit var editRep: Rep
    lateinit var updateButtom: Button
    val REQUEST_TAKE_PHOTO = 1

    private val memberViewModel by viewModels<MemberViewModel>()
    private val repViewModel by viewModels<RepViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_profile)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.rep_profile_toolbar_title)

        repProfileName = findViewById(R.id.input_rep_name)
        repProfileAddress = findViewById(R.id.input_rep_address)
        updateButtom = findViewById(R.id.btn_rep_update)
        imageProfile = findViewById(R.id.iv_rep_profile)
        imageProfile.setOnClickListener{ takePicture() }

        updateButtom.setOnClickListener { update() }

        val profileImage = PreferenceManager.getDefaultSharedPreferences(this).getString(MediaStore.EXTRA_OUTPUT, null)
        //if(profileImage != null) {
        //    photoURI = Uri.parse(profileImage)
        //     imageProfile.setImageURI(photoURI)
        // }
        //else {
        // photoURI = Uri.parse("/")
        imageProfile.setImageResource(R.mipmap.ic_launcher_round)
        //}

        memberViewModel.isLogged().observe(this, Observer {
            repViewModel.getRepById(it.repId!!).observe(this, Observer { it2 ->
                if(it2 != null) {
                    editRep = it2
                    repProfileName.setText(it2.name)
                    repProfileAddress.setText(it2.address)
                    //imageProfile.setImageURI(Uri.parse(it2.image))
                }
            })

        })
    }

    fun update() {
        if(repProfileName.text.toString().isNullOrEmpty() && repProfileAddress.text.toString().isNullOrEmpty()) {
            Toast.makeText(this,"Preencha ao menos o nome", Toast.LENGTH_SHORT).show()
        }
        else {
            editRep.apply {
                this.name = repProfileName.text.toString()
                this.address = repProfileAddress.text.toString()
                //this.image = photoURI.toString()

                repViewModel.update(this)
                val intent = Intent(this@RepProfileActivity, MyRepActivity::class.java)
                intent.change()
            }
        }
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