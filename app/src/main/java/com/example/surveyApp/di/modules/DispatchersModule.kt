package com.example.surveyApp.di.modules

import com.example.surveyApp.common.helpers.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
object DispatchersModule {

    @Provides
    fun providesDispatchersProvider() = DispatcherProvider()
}