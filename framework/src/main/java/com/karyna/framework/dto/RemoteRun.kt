package com.karyna.framework.dto

import com.google.firebase.Timestamp

data class RemoteRun(
    val userId: String = "",
    val userName: String = "",
    val userAvatarUrl: String = "",
    val date: Timestamp = Timestamp.now(),
    val locationCountry: String = "",
    val locationCity: String = "",
    //run characteristics
    val coordinates: String = "",
    val durationS: Long = 0,
    val distanceMeters: Int = 0,
    val paceMetersInS: Int = 0,
    val calories: Int? = null
)