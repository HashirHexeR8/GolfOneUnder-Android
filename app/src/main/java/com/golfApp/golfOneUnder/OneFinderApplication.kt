package com.golfApp.golfOneUnder

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class OneFinderApplication: Application() {

    init {
        instance = this
    }
    companion object {
        private var instance: OneFinderApplication? = null

        fun context() : OneFinderApplication {
            return instance as OneFinderApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}