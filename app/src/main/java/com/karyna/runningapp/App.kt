package com.karyna.runningapp

import android.app.Application
import com.karyna.feature.core.utils.StringFormatter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        StringFormatter.setRes(resources)
    }
}