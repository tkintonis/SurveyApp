package com.example.surveyApp.presentation.surveyScreen

import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.enums.AnswerResult
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.common.models.UiState
import com.example.surveyApp.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@PrepareForTest(SurveyViewModel::class, SurveyRepository::class)
class SurveyViewModelTest {

    private lateinit var repository: SurveyRepository
    private lateinit var viewModel: SurveyViewModel

    private lateinit var surveyResultEntries: MutableList<UiState<List<SurveyItem>>>
    private lateinit var surveyItemSizeEntries: MutableList<Int>
    private lateinit var submittedQuestionsEntries: MutableList<Int>

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = PowerMockito.mock(SurveyRepository::class.java)
        viewModel = SurveyViewModel(repository)
        surveyResultEntries = mutableListOf()
        surveyItemSizeEntries = mutableListOf()
        submittedQuestionsEntries = mutableListOf()
    }

    @Test
    fun whenFetchQuestions_shouldEmit_SuccessUiState() {
        runTest {
            val job = launch {
                viewModel.surveyItemsUiState.toList(surveyResultEntries)
            }
            val job2 = launch {
                viewModel.surveyItemSize.toList(surveyItemSizeEntries)
            }
            runCurrent()
            assertEquals(0, surveyResultEntries.size)

            assertEquals(1, surveyItemSizeEntries.size)
            assertEquals(0, surveyItemSizeEntries[0])

            Mockito.`when`(repository.fetchQuestions()).thenReturn(Resource.Success(listOf(QuestionDto(1, "question"))))
            viewModel.fetchQuestions()
            runCurrent()

            assertEquals(2, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[0])
            assertEquals(UiState.Success(listOf((SurveyItem(1, "question")))), surveyResultEntries[1])

            assertEquals(2, surveyItemSizeEntries.size)
            assertEquals(1, surveyItemSizeEntries[1])

            job.cancel()
            job2.cancel()
        }
    }

    @Test
    fun whenFetchQuestions_withError_shouldEmit_ErrorUiState() {
        val exception = Exception("error")
        runTest {
            val job = launch {
                viewModel.surveyItemsUiState.toList(surveyResultEntries)
            }
            runCurrent()
            assertEquals(0, surveyResultEntries.size)

            Mockito.`when`(repository.fetchQuestions()).thenReturn(Resource.Error(exception))
            viewModel.fetchQuestions()
            runCurrent()

            assertEquals(2, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[0])
            assertEquals(UiState.Error(exception), surveyResultEntries[1])
            job.cancel()
        }
    }

    @Test
    fun whenSubmitAnswer_withSuccess_shouldEmit_SuccessUiState() {
        val surveyItem = SurveyItem(1, "question", "answer", AnswerResult.NOT_SUBMITTED)
        val newItem = SurveyItem(1, "question", "answer", AnswerResult.SUCCESS)
        runTest {
            val job = launch {
                viewModel.surveyItemsUiState.toList(surveyResultEntries)
            }

            val job2 = launch {
                viewModel.submittedQuestions.toList(submittedQuestionsEntries)
            }
            runCurrent()
            assertEquals(0, surveyResultEntries.size)
            assertEquals(1, submittedQuestionsEntries.size)
            assertEquals(0, submittedQuestionsEntries[0])

            Mockito.`when`(repository.fetchQuestions()).thenReturn(Resource.Success(listOf(QuestionDto(1, "question"))))
            viewModel.fetchQuestions()
            runCurrent()
            assertEquals(2, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[0])
            assertEquals(UiState.Success(listOf((SurveyItem(1, "question")))), surveyResultEntries[1])

            assertEquals(1, submittedQuestionsEntries.size)
            assertEquals(0, submittedQuestionsEntries[0])

            Mockito.`when`(repository.submitAnswer(surveyItem)).thenReturn(Resource.Success(newItem))
            viewModel.submitAnswer(surveyItem)
            runCurrent()

            assertEquals(4, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[2])
            assertEquals(UiState.Success(listOf(newItem)), surveyResultEntries[3])

            assertEquals(2, submittedQuestionsEntries.size)
            assertEquals(1, submittedQuestionsEntries[1])

            job.cancel()
            job2.cancel()
        }

    }

    @Test
    fun whenSubmitAnswer_withError_shouldEmit_ErrorUiState() {
        val surveyItem = SurveyItem(1, "question", "answer", AnswerResult.NOT_SUBMITTED)
        val exception = Exception("error")
        runTest {
            val job = launch {
                viewModel.surveyItemsUiState.toList(surveyResultEntries)
            }
            runCurrent()
            assertEquals(0, surveyResultEntries.size)

            Mockito.`when`(repository.submitAnswer(surveyItem)).thenReturn(Resource.Error(exception))
            viewModel.submitAnswer(surveyItem)
            runCurrent()

            assertEquals(2, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[0])
            assertEquals(UiState.Error(exception), surveyResultEntries[1])
            job.cancel()
        }
    }

    @Test
    fun whenRetryAnswer_withSuccess_shouldEmit_SuccessUiState() {
        val surveyItem = SurveyItem(1, "question", "answer", AnswerResult.NOT_SUBMITTED)
        val newItem = SurveyItem(1, "question", "answer", AnswerResult.FAIL)
        runTest {
            val job = launch {
                viewModel.surveyItemsUiState.toList(surveyResultEntries)
            }

            val job2 = launch {
                viewModel.submittedQuestions.toList(submittedQuestionsEntries)
            }

            runCurrent()
            assertEquals(0, surveyResultEntries.size)
            assertEquals(1, submittedQuestionsEntries.size)
            assertEquals(0, submittedQuestionsEntries[0])

            Mockito.`when`(repository.fetchQuestions()).thenReturn(Resource.Success(listOf(QuestionDto(1, "question"))))
            viewModel.fetchQuestions()
            runCurrent()
            assertEquals(2, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[0])
            assertEquals(UiState.Success(listOf((SurveyItem(1, "question")))), surveyResultEntries[1])

            assertEquals(1, submittedQuestionsEntries.size)
            assertEquals(0, submittedQuestionsEntries[0])

            Mockito.`when`(repository.submitAnswer(surveyItem)).thenReturn(Resource.Success(newItem))
            viewModel.submitAnswer(surveyItem)
            runCurrent()

            assertEquals(4, surveyResultEntries.size)
            assertEquals(UiState.Loading(), surveyResultEntries[2])
            assertEquals(UiState.Success(listOf(newItem)), surveyResultEntries[3])

            assertEquals(2, submittedQuestionsEntries.size)
            assertEquals(1, submittedQuestionsEntries[1])


            viewModel.retryAnswer(newItem)
            runCurrent()
            assertEquals(3, submittedQuestionsEntries.size)
            assertEquals(0, submittedQuestionsEntries[2])

            assertEquals(5, surveyResultEntries.size)
            assertEquals(UiState.Success(listOf(SurveyItem(1,"question","", AnswerResult.NOT_SUBMITTED))), surveyResultEntries[4])

            job.cancel()
            job2.cancel()
        }
    }
}