package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.cancelNotifications
import com.udacity.util.sendNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var url: String? = null
    private var title: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(getString(R.string.download_notification_channel_id),getString(R.string.download_notification_channel_name))


        binding.contentMainLayout.customButton.setOnClickListener {
            binding.contentMainLayout.customButton.buttonState = ButtonState.Clicked

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.cancelNotifications()
            when (binding.contentMainLayout.radioGroup.checkedRadioButtonId) {
                R.id.radioButton1 -> {
                    url =URL_GLIDE
                    title =  getString(R.string.glide_image_loading_library_by_bumbtch)
                }
                R.id.radioButton2 -> {
                    url =URL_UDACITY
                    title =  getString(R.string.loadapp_current_repository_by_udacity)
                }
                R.id.radioButton3 -> {
                    url =URL_RETROFIT
                    title =  getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_square_inc)
                }
                else -> {
                    Toast.makeText(this,"Please Select the file to download",Toast.LENGTH_SHORT).show()
                }
            }
            if(url != null)
                download(url!!)
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(id == downloadID){
                Log.d("TEST", "Download Complete")
                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val query = DownloadManager.Query().setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Log.d("TEST", "Download Success")
                            binding.contentMainLayout.customButton.buttonState = ButtonState.Completed
                            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.sendNotification(url!!,title, true,context)
                        }
                        DownloadManager.STATUS_FAILED -> {
                            Log.d("TEST", "Download Failed")
                            binding.contentMainLayout.customButton.buttonState = ButtonState.Completed
                            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.sendNotification(url!!,title, false,context)
                        }
                    }
                }

                cursor.close()
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,channelName,
                NotificationManager.IMPORTANCE_LOW)
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description ="Download Complete"


            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun download(url :String) {
        binding.contentMainLayout.customButton.buttonState = ButtonState.Loading
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL_GLIDE =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_UDACITY =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_RETROFIT =
            "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }
}