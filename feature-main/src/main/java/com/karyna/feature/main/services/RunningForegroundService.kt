package com.karyna.feature.main.services

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.run.RunInput
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.core.utils.utils.DateUtils.toIsoDate
import com.karyna.feature.core.utils.utils.flowRepeatEvery
import com.karyna.feature.main.map.LocationProvider
import com.karyna.feature.main.map.RunUiInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt
import com.karyna.feature.core.R as RCore

@AndroidEntryPoint
class RunningForegroundService : Service() {

    @Inject
    lateinit var repository: RunningRepository

    @Inject
    @Named("userId")
    lateinit var userId: String

    private val scope by lazy { CoroutineScope(Dispatchers.Default) }
    var timerJob: Job? = null

    private val _uiState = MutableStateFlow(RunUiInfo.EMPTY)
    val uiState: StateFlow<RunUiInfo> = _uiState

    private var runDurationS: Long = 0
    private var distanceM: Int = 0
    private var date = OffsetDateTime.now()

    private val binder = RunningBinder()
    private val locationProvider by lazy { LocationProvider(this) }

    inner class RunningBinder : Binder() {
        fun getService(): RunningForegroundService = this@RunningForegroundService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        observeLocation()
        startTimer()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        finishTimer()
    }

    fun finishRun() {
        finishTimer()
        scope.launch {
            saveRun()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun observeLocation() = with(locationProvider) {
        trackUserRun()
        liveLocations.observeForever { locations ->
            _uiState.update { _uiState.value.copy(userPath = locations) }
        }

        liveDistance.observeForever { distance ->
            distanceM = distance
            _uiState.update {
                _uiState.value.copy(formattedDistance = StringFormatter.from(RCore.string.n_meters, distance))
            }
        }
        livePace.observeForever { pace ->
            _uiState.update {
                _uiState.value.copy(formattedPace = StringFormatter.from(RCore.string.format_min_per_km, pace))
            }
        }
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
            .setContentTitle("Run in progress")
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

    private fun startTimer() {
        timerJob = scope.launch(Dispatchers.Main) {
            flowRepeatEvery(1000).collectLatest {
                runDurationS += 1
                _uiState.update {
                    _uiState.value.copy(duration = DateUtils.formatElapsedTime(runDurationS))
                }
            }
        }
    }

    private fun finishTimer() {
        timerJob?.cancel()
    }

    private suspend fun saveRun() =
        with(_uiState.value) {
            val userResult = repository.getUser(userId)
            val locationShortResult =
                repository.getLocationShort(userPath.first().run { LatLng(latitude, longitude) })
            if (userResult.isSuccess && locationShortResult.isSuccess) {
                val user = userResult.getOrThrow()
                val location = locationShortResult.getOrThrow()

                val result = repository.saveRun(
                    RunInput(
                        userId = user.id,
                        userName = user.name,
                        userAvatarUrl = user.avatarUrl,
                        date = date.toIsoDate(),
                        location = location,
                        coordinates = userPath.map { LatLng(it.latitude, it.longitude) },
                        durationS = runDurationS,
                        distanceMeters = distanceM,
                        paceMetersInS = if (runDurationS <= 0) 0f else (distanceM / runDurationS.toFloat()),
                        calories = user.weight?.let { calculateKCaloriesBurned(it, distanceM) }
                    )
                )
                if (result.isFailure) {
                    Timber.e(result.exceptionOrNull())
                }
            }
        }


    private fun calculateKCaloriesBurned(weight: Float, distanceM: Int): Int {
        return ((distanceM / 1000f) * weight).roundToInt()
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
    }
}