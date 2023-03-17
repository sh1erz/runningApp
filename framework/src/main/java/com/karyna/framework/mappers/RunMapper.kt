package com.karyna.framework.mappers

import com.karyna.core.domain.LatLng
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
        durationMs = durationMs,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}

fun runToDto(run: DomainRun, user: User) = with(run) {
    Run(
        id = id,
        date = date,
        location = location,
        userId = user.id,
        coordinates = coordinates as ArrayList<LatLng>,
        durationMs = durationMs,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}