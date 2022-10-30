package com.example.surveyApp

import android.app.Application
import com.example.surveyApp.di.components.ApplicationComponent
import com.example.surveyApp.di.components.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}