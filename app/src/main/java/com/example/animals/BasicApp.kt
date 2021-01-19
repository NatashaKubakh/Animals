package com.example.animals

import android.app.Application
import android.content.Context

class BasicApp : Application() {

    companion object {
        private var app: BasicApp? = null
        var context: Context? = null

        fun getInstance(): BasicApp {
            app?.let { return it }
            val application = BasicApp()
            app = application
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        app = BasicApp()
    }
}