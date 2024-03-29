package com.karyna.core.domain.run

import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort

data class Run(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatarUrl: String,
    val date: String,
    val location: LocationShort,
    //run characteristics
    val coordinates: List<LatLng>,
    val durationS: Long,
    val distanceMeters: Int,
    val paceMetersInS: Float,
    val calories: Int?
)