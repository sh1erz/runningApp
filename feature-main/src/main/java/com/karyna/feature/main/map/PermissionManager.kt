package com.karyna.feature.main.map

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionsManager constructor(
    activity: AppCompatActivity,
    onGranted: () -> Unit
) {

    private val locationPermissionProvider =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                onGranted()
            }
        }

    fun requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}