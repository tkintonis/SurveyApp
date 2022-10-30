package com.example.surveyApp.di.components

import android.content.Context
import com.example.surveyApp.activities.MainActivity
import com.example.surveyApp.di.common.ActivityScope
import com.example.surveyApp.di.modules.FragmentSubComponentModule
import com.example.surveyApp.presentation.startingScreen.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [FragmentSubComponentModule::class])
interface ActivitySubcomponent {

    fun fragmentComponent(): FragmentSubComponent.Factory
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)


    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ActivitySubcomponent
    }
}