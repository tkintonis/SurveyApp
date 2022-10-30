package com.example.surveyApp.presentation.startingScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.surveyApp.activities.MainActivity
import com.example.surveyApp.common.helpers.safeNavigate
import com.example.surveyApp.databinding.MainFragmentLayoutBinding
import com.example.surveyApp.di.components.FragmentSubComponent

class MainFragment : Fragment() {

    private lateinit var fragmentSubComponent: FragmentSubComponent
    private var binding: MainFragmentLayoutBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSubComponent = (activity as MainActivity).activitySubcomponent.fragmentComponent().create(this)
        fragmentSubComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentLayoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.run {
            startButton.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToSurveyFragment()
                findNavController().safeNavigate(action)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}