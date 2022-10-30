package com.example.surveyApp.di.components

import android.content.Context
import com.example.surveyApp.di.common.ApplicationScope
import com.example.surveyApp.di.modules.ActivitySubComponentModule
import com.example.surveyApp.di.modules.CommonsModule
import com.example.surveyApp.di.modules.DispatchersModule
import com.example.surveyApp.di.modules.NetworkServiceModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@ApplicationScope
@Singleton
@Component(modules = [DispatchersModule::class, NetworkServiceModule::class, ActivitySubComponentModule::class, CommonsModule::class])
interface ApplicationComponent {

    fun activityComponent(): ActivitySubcomponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}