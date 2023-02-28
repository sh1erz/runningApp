package com.karyna.feature.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.main.map.LocationProvider
import com.karyna.feature.main.map.Ui
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MapViewModel @Inject constructor() :
    ViewModel(), DefaultLifecycleObserver {
    val ui = MutableLiveData(Ui.EMPTY)

    private var locationProvider by Delegates.notNull<LocationProvider>()
    private var trackLocation = false

    fun init(activity: AppCompatActivity) {
        locationProvider = LocationProvider(activity)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val current = ui.value
        locationProvider.liveLocations.observe(owner) { locations ->
            ui.value = current?.copy(userPath = locations)
        }

        locationProvider.liveLocation.observe(owner) { currentLocation ->
            ui.value = current?.copy(currentLocation = currentLocation)
        }

        locationProvider.liveDistance.observe(owner) { distance ->
            ui.value = current?.copy(formattedDistance = StringFormatter.from(R.string.distance_value, distance))
        }
    }


    fun getUserLocation() {
        locationProvider.getUserLocation()
    }

    fun toggleTrackingLocation() {
        trackLocation = !trackLocation
        if (trackLocation) {
            locationProvider.trackUser()
        } else {
            locationProvider.stopTracking()
        }
    }
}