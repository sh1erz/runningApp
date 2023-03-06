package com.karyna.feature.main.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.karyna.feature.core.R as RCore

/**Should save data and pass events by bind interface*/
class RunningForegroundService : Service() {
    private val binder = RunningBinder()

    inner class RunningBinder : Binder() {
        fun getService(): RunningForegroundService = this@RunningForegroundService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent?.getStringExtra(EXTRA) ?: ""
        val notificationIntent = requireNotNull(packageManager.getLaunchIntentForPackage(packageName))
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        )
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Run")
            .setContentText(input)
            .setSmallIcon(RCore.drawable.ic_baseline_person_24)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val name = getString(RCore.string.important_notifications)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        const val EXTRA = "service_extra"
        private const val CHANNEL_ID = "CHANNEL_ID"
    }
}