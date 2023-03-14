package com.karyna.feature.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

class LocationProvider(private val context: Context) {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val locations = mutableListOf<LatLng>()
    private var distance = 0
    private val runLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val currentLocation = result.lastLocation
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val lastLocation = locations.lastOrNull()

            if (lastLocation != null) {
                distance += SphericalUtil.computeDistanceBetween(lastLocation, latLng).roundToInt()
                liveDistance.value = distance
            }
            if (latLng != locations.lastOrNull()) {
                locations.add(latLng)
                liveLocations.value = locations
            }
        }
    }
    private val currentLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val currentLocation = result.lastLocation
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            liveLocation.value = latLng
        }
    }
    val liveLocation = MutableLiveData<LatLng>()
    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()

    //4
    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        client.removeLocationUpdates(runLocationCallback)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        client.requestLocationUpdates(
            locationRequest, currentLocationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun trackUserRun() {
        client.removeLocationUpdates(currentLocationCallback)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 500
        client.requestLocationUpdates(locationRequest, runLocationCallback, Looper.getMainLooper())
    }

    fun stopTracking() {
        locations.clear()
        distance = 0
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            client.removeLocationUpdates(currentLocationCallback)
        }
    }


}