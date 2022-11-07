package com.mlr_apps.freetogame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FreeToGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}