package com.karyna.feature.main.map

import com.google.android.gms.maps.model.LatLng

data class RunUiInfo(
    val formattedPace: String,
    val formattedDistance: String,
    val duration: String,
    val userPath: List<LatLng>,
    val caloriesBurned: String? = null
) {

    companion object {

        val EMPTY = RunUiInfo(
            formattedPace = "",
            userPath = emptyList(),
            duration = "",
            formattedDistance = "",
            caloriesBurned = null
        )
    }
}