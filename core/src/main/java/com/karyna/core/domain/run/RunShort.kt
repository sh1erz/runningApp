package com.karyna.core.domain.run

import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User

data class RunShort(
    val id: String,
    val date: String,
    val location: LocationShort,
    val user: User,
    val durationMs: Long,
    val distanceMeters: Int,
    val paceMetersInS: Int
)