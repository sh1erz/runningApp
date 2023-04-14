package com.karyna.core.domain.run

import com.karyna.core.domain.LocationShort

data class RunShort(
    val id: Long,
    val userId: String,
    val userName: String,
    val date: String,
    val location: LocationShort,
    val durationS: Long,
    val distanceMeters: Int,
    val paceMetersInS: Int
)