package com.karyna.feature.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.main.map.LocationProvider
import com.karyna.feature.main.map.RunInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MapViewModel @Inject constructor() :
    ViewModel(), DefaultLifecycleObserver {
    val runInfo = MutableLiveData(RunInfo.EMPTY)
    val currentLocation = MutableLiveData<LatLng?>(null)

    private var locationProvider by Delegates.notNull<LocationProvider>()

    fun init(activity: AppCompatActivity) {
        locationProvider = LocationProvider(activity)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        locationProvider.liveLocation.observe(owner) { currentLocation ->
            this.currentLocation.value = currentLocation
        }

        locationProvider.liveLocations.observe(owner) { locations ->
            runInfo.value = runInfo.value?.copy(userPath = locations)
        }

        locationProvider.liveDistance.observe(owner) { distance ->
            runInfo.value =
                runInfo.value?.copy(formattedDistance = StringFormatter.from(R.string.distance_value, distance))
        }
    }


    fun getUserLocation() {
        locationProvider.getUserLocation()
    }

    fun trackLocation(enable: Boolean) {
        if (enable) {
            locationProvider.trackUser()
        } else {
            locationProvider.stopTracking()
        }
    }
}