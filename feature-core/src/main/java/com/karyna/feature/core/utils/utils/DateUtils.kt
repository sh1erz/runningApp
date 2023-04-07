package com.karyna.feature.core.utils.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun OffsetDateTime.toIsoDate(): String = format(DateTimeFormatter.ISO_DATE_TIME)
}