package com.karyna.core.domain.run

import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User

data class RunInput(
    val date: String,
    val location: LocationShort,
    val user: User,
    //run characteristics
    val coordinates: List<LatLng>,
    val durationMs: Long,
    val distanceMeters: Int,
    val paceMetersInS: Int,
    val calories: Int?
)