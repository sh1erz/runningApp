package com.karyna.framework.mappers

import com.karyna.core.domain.LatLng
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.dto.Run
import com.karyna.framework.dto.User
import com.karyna.core.domain.run.Run as DomainRun

fun runToDomain(run: Run, user: User) = with(run) {
    DomainRun(
        id = id,
        date = date,
        location = location,
        userId = user.id,
        userName = user.name,
        coordinates = coordinates,
        durationS = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories,
    )
}

fun runInputToDto(
    id: String,
    runInput: RunInput
) = with(runInput) {
    Run(
        id = id,
        date = date,
        location = location,
        userId = userId,
        userName = userName,
        coordinates = coordinates as ArrayList<LatLng>,
        durationS = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}

fun runInputToDomain(
    id: String,
    runInput: RunInput
) = with(runInput) {
    DomainRun(
        id = id,
        userId = userId,
        userName = userName,
        date = date,
        location = location,
        coordinates = coordinates as ArrayList<LatLng>,
        durationS = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}