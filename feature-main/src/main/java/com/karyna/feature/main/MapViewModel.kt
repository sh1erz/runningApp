package com.karyna.feature.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.karyna.feature.main.map.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MapViewModel @Inject constructor() :
    ViewModel(), DefaultLifecycleObserver {
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
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        locationProvider.liveLocation.removeObservers(owner)
    }

    fun trackCurrentLocation() {
        locationProvider.getUserLocation()
    }

    fun stopTracking() {
        locationProvider.stopTracking()
    }
}