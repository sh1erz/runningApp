package com.karyna.feature.main.services

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.main.R
import com.karyna.feature.main.map.LocationProvider
import com.karyna.feature.main.map.RunInfo
import com.karyna.feature.core.R as RCore

class RunningForegroundService : Service() {
    val runInfo = MutableLiveData(RunInfo.EMPTY)

    private val binder = RunningBinder()
    private val locationProvider by lazy { LocationProvider(this) }

    inner class RunningBinder : Binder() {
        fun getService(): RunningForegroundService = this@RunningForegroundService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        observeLocation()
        return START_NOT_STICKY
    }

    fun finishRun() {
        //todo save data
        runInfo.value = RunInfo.EMPTY
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun observeLocation() {
        locationProvider.trackUserRun()
        locationProvider.liveLocations.observeForever { locations ->
            runInfo.value = runInfo.value?.copy(userPath = locations)
        }

        locationProvider.liveDistance.observeForever { distance ->
            runInfo.value =
                runInfo.value?.copy(formattedDistance = StringFormatter.from(R.string.distance_value, distance))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy service")
    }
    private fun createNotification(): Notification {
        val notificationIntent = requireNotNull(packageManager.getLaunchIntentForPackage(packageName))
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        )
        createNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Run")
            .setContentText("")
            .setSmallIcon(RCore.drawable.ic_baseline_person_24)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        val name = getString(RCore.string.important_notifications)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
    }
}