package com.example.norifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit


import androidx.work.*


class MyWorker(context: Context,workerParams:WorkerParameters): Worker(context,workerParams) {

    companion object{
        private val CHANNEL_ID = "channel_id"
        private val NOTIFICATION_ID = 1

    }
    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification(){
        val intent = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent= PendingIntent.getActivity(applicationContext,1,intent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val siuIntent= Intent(applicationContext,Celebration::class.java)
        val siuPendingIntent= PendingIntent.getActivity(applicationContext,2,siuIntent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val sadIntent= Intent(applicationContext,Sad::class.java)
        val sadPendingIntent= PendingIntent.getActivity(applicationContext,2,sadIntent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("My notification")
            .setContentText("Hast du es gemacht oder willst du sterben ?! ")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_siu,"Done it all!",siuPendingIntent)
            .addAction(R.drawable.ic_sad,"I wasn´t able to do it" ,sadPendingIntent)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID, builder.build())
        }
    }

}