package com.karyna.runningapp.utils

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

object StringFormatter {

    private lateinit var resources: Resources

    fun setRes(resources: Resources) {
        this.resources = resources
    }

    fun from(@StringRes formatter: Int, vararg args: Any): String =
        resources.getString(formatter, *args)

    fun from(@StringRes formatter: Int, vararg args: String): String =
        resources.getString(formatter, *args)

    fun from(@StringRes formatter: Int, vararg args: Int): String =
        resources.getString(formatter, *args.toTypedArray())

    fun from(@StringRes formatter: Int, vararg args: Double): String =
        resources.getString(formatter, *args.toTypedArray())

    fun from(@StringRes formatter: Int, vararg args: Float): String =
        resources.getString(formatter, *args.toTypedArray())

    fun from(@StringRes id: Int): String =
        resources.getString(id)

    fun fromPlurals(@PluralsRes id: Int, quantity: Int, arg: Int): String =
        resources.getQuantityString(id, quantity, arg)

    fun fromPlurals(@PluralsRes id: Int, quantity: Int): String =
        resources.getQuantityString(id, quantity)

    fun fromPlurals(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String =
        resources.getQuantityString(id, quantity, *formatArgs)

}