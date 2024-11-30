package com.capstone.arabicmorph.gamification.util

import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.capstone.arabicmorph.R

class NotificationUtils {

    private val CHANNEL_ID = "challenge_channel_id"
    private val CHANNEL_NAME = "Challenge Notifications"

    fun sendChallengeCompletedNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for challenge completed notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Tantangan Harian Selesai!")
            .setContentText("Selamat! Anda telah mempelajari 5 kata baru dan mendapatkan 10 XP.")
            .setSmallIcon(R.drawable.ic_notif) // Pastikan ikon ini ada
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
