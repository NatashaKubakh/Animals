package com.example.animals

import android.app.Application
import android.content.Context

class BasicApp : Application() {

    companion object {
        lateinit var app: BasicApp
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        context = app.applicationContext
    }
}