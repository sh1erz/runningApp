package com.karyna.framework.mappers

import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.framework.dto.Run
import com.karyna.framework.dto.User
import com.karyna.core.domain.run.Run as DomainRun

fun runToDomain(run: Run, user: User) = with(run) {
    DomainRun(
        id = id,
        date = date,
        location = location,
        user = userToDomain(user),
        coordinates = coordinates,
        durationMs = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}

fun runInputToDto(
    userId: String,
    date: String,
    location: LocationShort,
    coordinates: List<LatLng>,
    durationS: Long,
    distanceMeters: Int,
    paceMetersInS: Int,
    calories: Int?
) = Run(
    id = 0,
    date = date,
    location = location,
    userId = userId,
    coordinates = coordinates as ArrayList<LatLng>,
    durationS = durationS,
    distanceMeters = distanceMeters,
    paceMetersInS = paceMetersInS,
    calories = calories
)