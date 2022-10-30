package com.example.surveyApp.di.modules

import android.content.Context
import android.content.res.Resources
import com.example.surveyApp.di.common.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonsModule {

    @Singleton
    @Provides
    fun getSystemResources(@ApplicationScope context: Context) : Resources {
        return context.resources
    }
}