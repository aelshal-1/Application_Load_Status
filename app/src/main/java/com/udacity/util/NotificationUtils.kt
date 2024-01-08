package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(messageBody: String, name:String, status: Boolean,applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    Log.d("Test","name IS $name")
    contentIntent.putExtra("name",name)
    contentIntent.putExtra("status",status)
    contentIntent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext, NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id))



    Log.d("TEST","SEND NOTIFICATION")
    builder.setSmallIcon(R.drawable.download_completed)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.download_completed,
            applicationContext.getString(R.string.check_status),
            pendingIntent
        )
    notify(NOTIFICATION_ID,builder.build())
}
fun NotificationManager.cancelNotifications(){
    cancelAll()
}