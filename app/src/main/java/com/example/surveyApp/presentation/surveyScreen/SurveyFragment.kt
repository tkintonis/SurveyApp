package com.example.surveyApp.presentation.surveyScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.surveyApp.R
import com.example.surveyApp.activities.MainActivity
import com.example.surveyApp.common.helpers.*
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.common.models.UiState
import com.example.surveyApp.databinding.SurveyFragmentLayoutBinding
import com.example.surveyApp.di.components.FragmentSubComponent
import com.example.surveyApp.presentation.surveyScreen.adapters.SurveyViewPagerAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveyFragment : Fragment() {

    private lateinit var fragmentSubComponent: FragmentSubComponent

    @Inject
    lateinit var factory: SurveyViewModel.Factory
    private lateinit var viewModel: SurveyViewModel

    private var binding: SurveyFragmentLayoutBinding? = null
    private var surveyViewPagerAdapter: SurveyViewPagerAdapter? = null
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding?.run {
                questionProgressTV.text = resources.getString(
                    R.string.questions_progress,
                    position + 1,
                    viewModel.surveyItemSize.value)
                previousButton.isActivated = position > 0
                nextButton.isActivated = ((position < viewModel.surveyItemSize.value.minus(1)))
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSubComponent = (activity as MainActivity).activitySubcomponent.fragmentComponent().create(this)
        fragmentSubComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[SurveyViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SurveyFragmentLayoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupToolbarViews()
        setupQuestionsViewPager()

        repeatWithLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.surveyItemsUiState.collect { state ->
                    handleUiState(state)
                }
            }

            launch {
                viewModel.submittedQuestions.collect {
                    setupSubmitAnswersProgress(it)
                }
            }

            launch {
                viewModel.surveyItemSize.collect {
                    updateToolbarViews(it)
                }
            }
        }

        viewModel.fetchQuestions()
    }

    private fun setupSubmitAnswersProgress(questionsSubmitted: Int) {
        binding?.run {
            submitProgress.text = resources.getString(
                R.string.submitted_questions_progress, questionsSubmitted
            )
        }
    }

    private fun handleUiState(state: UiState<List<SurveyItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding?.run {
                    progressBar.show()
                }
            }
            is UiState.Success -> {
                binding?.run {
                    progressBar.hide()
                    if (state.data.isNullOrEmpty()) return
                    surveyViewPagerAdapter?.setList(state.data.toList())
                }
            }
            is UiState.Error -> {
                binding?.run {
                    progressBar.hide()
                    showToastMessage(root, state.exception.message ?: "", resources)
                }
            }
        }
    }

    private fun setupQuestionsViewPager() {
        surveyViewPagerAdapter = SurveyViewPagerAdapter(
            submitAnswer = { item ->
                viewModel.submitAnswer(item)
                binding?.run { hideKeyboard(this.root) }
            },
            retry = { item ->
                viewModel.retryAnswer(item)
            }
        )
        binding?.run {
            with(questionsViewPager) {
                adapter = surveyViewPagerAdapter
                isUserInputEnabled = false
                registerOnPageChangeCallback(onPageChangeCallback)
            }
        }
    }

    private fun setupToolbar() {
        binding?.run {
            toolbar.setupWithNavController(findNavController())
            toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
        }
    }


    private fun setupToolbarViews() {
        binding?.run {
            nextButton.setOnClickListener {
                questionsViewPager.setCurrentItem(questionsViewPager.currentItem + 1, true)
            }

            previousButton.setOnClickListener {
                questionsViewPager.setCurrentItem(questionsViewPager.currentItem - 1, true)
            }
        }
    }

    private fun updateToolbarViews(surveyListItemSize: Int) {
        binding?.run {
            questionProgressTV.text = resources.getString(
                    R.string.questions_progress,
                    questionsViewPager.currentItem + 1,
                    surveyListItemSize)

            nextButton.isActivated = (questionsViewPager.currentItem < surveyListItemSize - 1)
        }
    }

    override fun onDestroyView() {
        binding?.run { questionsViewPager.unregisterOnPageChangeCallback(onPageChangeCallback) }
        binding = null
        super.onDestroyView()
    }

}