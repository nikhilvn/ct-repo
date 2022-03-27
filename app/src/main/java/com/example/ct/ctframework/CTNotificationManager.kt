package com.example.ct.ctframework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ct.MainActivity
import com.example.ct.R

private const val CT_NOTIFICATION_CHANNEL_ID = "ct_notification_channel_id"
const val CT_NOTIFICATION_ID = 10

class CTNotificationManager(private val context: Context) {

    private lateinit var notificationManager: NotificationManager

    fun notifyGoalNearlyComplete(progress: Int) {
        val title = context.getString(R.string.notifyGoalNearlyComplete_title)
        val text = "You have completed ${progress}% of your goal. Keep it up!"
        val priority = NotificationCompat.PRIORITY_HIGH
        sendNotification(title, text, priority)
    }

    private fun sendNotification(contentTitle: String, contentText: String, priority: Int) {
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(CT_NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ct_notification_channel_name"
            val descriptionText = "ct_notification_channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CT_NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            // Register the channel with the system
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}