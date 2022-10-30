package com.example.surveyApp.di.components

import androidx.fragment.app.Fragment
import com.example.surveyApp.di.common.FragmentScope
import com.example.surveyApp.presentation.startingScreen.MainFragment
import com.example.surveyApp.presentation.surveyScreen.SurveyFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [])
interface FragmentSubComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: SurveyFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): FragmentSubComponent
    }
}