package com.example.readdit.ui.profile.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.readdit.R
import com.example.readdit.ui.home.HomeFragment

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Initialize intents
        val i = Intent(context, HomeFragment::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0,i,0)

        // Configure notification content
        val builder = NotificationCompat.Builder(context!!, "readdit")
            .setSmallIcon(R.drawable.readdit_icon)
            .setContentTitle("Daily Reading Reminder")
            .setContentText("Don't forget your daily dose of articles!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())
    }
}