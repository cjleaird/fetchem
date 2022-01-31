package com.leaird.fetchem

import android.app.Application
import com.leaird.fetchem.model.prefs.NetworkPreferences
import com.leaird.fetchem.model.prefs.UserPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FetchEmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserPreferences.init(this)
        NetworkPreferences.init(this)
    }
}