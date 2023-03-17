package com.karyna.framework.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.karyna.core.domain.LatLng
import timber.log.Timber

object Converters {
    @TypeConverter
    fun fromCoordinates(value: ArrayList<LatLng>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCoordinates(value: String): ArrayList<LatLng> = try {
        Gson().fromJson(value)
    } catch (ex: JsonSyntaxException) {
        Timber.e(ex.message)
        ArrayList()
    }

    private inline fun <reified T> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)
}