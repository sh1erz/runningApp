package com.karyna.framework.mappers

import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.dto.LocalRun
import com.karyna.framework.dto.RemoteRun
import com.karyna.framework.dto.User
import java.text.SimpleDateFormat
import java.util.*
import com.karyna.core.domain.run.Run as DomainRun

fun runToDomain(localRun: LocalRun, user: User) = with(localRun) {
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

fun runInputToRun(
    id: String,
    runInput: RunInput
) = with(runInput) {
    LocalRun(
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

fun runInputToDto(
    runInput: RunInput
) = with(runInput) {
    RemoteRun(
        userId = userId,
        userName = userName,
        date = Timestamp(isoToDate(date)),
        locationCity = location.city,
        locationCountry = location.country,
        coordinates = Gson().toJson(coordinates),
        durationS = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}

fun remoteRunInputToDomain(
    id: String,
    runInput: RemoteRun
) = with(runInput) {
    DomainRun(
        id = id,
        userId = userId,
        userName = userName,
        date = dateToIso(date.toDate()),
        location = LocationShort(locationCountry, locationCity),
        coordinates = Gson().fromJson(coordinates, object : TypeToken<List<LatLng>>() {}.type),
        durationS = durationS,
        distanceMeters = distanceMeters,
        paceMetersInS = paceMetersInS,
        calories = calories
    )
}

private fun isoToDate(iso8601String: String): Date {
    val format = SimpleDateFormat(ISO_FORMAT, Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone(TIME_ZONE)
    return format.parse(iso8601String) ?: throw IllegalArgumentException("Invalid date format")
}

fun dateToIso(date: Date): String {
    val format = SimpleDateFormat(ISO_FORMAT, Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone(TIME_ZONE)
    return format.format(date)
}

private const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val TIME_ZONE = "UTC"