package com.example.happy

import android.app.LauncherActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.example.happy.model.Notification
import com.example.happy.model.Rep
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.NotificationViewModel
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
    lateinit var editMember: Member
    lateinit var updateButtom: Button
    val REQUEST_TAKE_PHOTO = 1

    private val memberViewModel by viewModels<MemberViewModel>()
    private val repViewModel by viewModels<RepViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelId = "com.example.happy.notification"

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
            editMember = it
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
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val notification = Notification(memberId = this.id,
                        component = Notification.Components.MYREP,
                        content = "Perfil da República atualizado por ${editMember.name} em ${currentDate}",
                        date = currentDate.toString() )
                notificationViewModel.create(notification)
                sendNotification("Perfil da República atualizado por ${editMember.name} em ${currentDate}")
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

    fun sendNotification(description: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = android.app.Notification.Builder(this, channelId)
                .setContentTitle("Happy")
                .setContentText(description)
                .setSmallIcon(R .mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher_round))
                .setContentIntent(pendingIntent)

        notificationManager.notify(Math.random().toInt(), builder.build())
    }
}