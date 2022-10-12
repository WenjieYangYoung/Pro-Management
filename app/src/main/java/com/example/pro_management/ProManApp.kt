package com.example.pro_management

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class ProManApp:Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}