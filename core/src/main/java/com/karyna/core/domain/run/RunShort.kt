package com.karyna.core.domain.run

import com.karyna.core.domain.LocationShort

data class RunShort(
    val id: Long,
    val date: String,
    val location: LocationShort,
    val userName: String,
    val durationMs: Long,
    val distanceMeters: Int,
    val paceMetersInS: Int
)