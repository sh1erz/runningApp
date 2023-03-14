package com.karyna.feature.main.map

import com.google.android.gms.maps.model.LatLng

data class RunInfo(
    val formattedPace: String,
    val formattedDistance: String,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = RunInfo(
            formattedPace = "",
            formattedDistance = "",
            userPath = emptyList()
        )
    }
}