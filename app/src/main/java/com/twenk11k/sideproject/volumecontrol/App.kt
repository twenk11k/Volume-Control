package com.twenk11k.sideproject.volumecontrol

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var application: Application

        fun getContext(): Context {
            return application.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}