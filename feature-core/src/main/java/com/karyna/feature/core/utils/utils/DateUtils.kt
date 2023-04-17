package com.karyna.feature.core.utils.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private const val DD_MM_YYYY = "dd.MM.yyyy"

    fun OffsetDateTime.toIsoDate(): String = withNano(0).format(DateTimeFormatter.ISO_DATE_TIME)

    fun formatIsoDate(isoDate: String): String =
        OffsetDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern(DD_MM_YYYY))
}