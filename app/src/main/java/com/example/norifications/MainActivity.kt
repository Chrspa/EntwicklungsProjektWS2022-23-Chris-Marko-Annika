package com.example.norifications


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.norifications.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val CHANNEL_ID = "channel_id"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notibtn.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification(){
        val intent= Intent(this,MainActivity::class.java)
        val pendingIntent= PendingIntent.getActivity(this,1,intent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val siuIntent= Intent(this,Celebration::class.java)
        val siuPendingIntent= PendingIntent.getActivity(this,2,siuIntent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val sadIntent= Intent(this,Sad::class.java)
        val sadPendingIntent= PendingIntent.getActivity(this,2,sadIntent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        })

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("My notification")
            .setContentText("Hast du es gemacht oder willst du sterben ?! ")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_siu,"Done it all!",siuPendingIntent)
            .addAction(R.drawable.ic_sad,"I'm dead",sadPendingIntent)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}