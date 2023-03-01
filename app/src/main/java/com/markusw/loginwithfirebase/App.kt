package com.markusw.loginwithfirebase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}