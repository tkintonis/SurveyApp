package com.example.surveyApp.presentation.surveyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.enums.AnswerResult
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.common.models.UiState
import com.example.surveyApp.network.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveyViewModel(private val repository: SurveyRepository) : ViewModel() {

    private val _surveyItemsUiState = MutableSharedFlow<UiState<List<SurveyItem>>>()
    val surveyItemsUiState = _surveyItemsUiState.asSharedFlow()

    private val itemList : MutableList<SurveyItem> = mutableListOf()

    private val _surveyItemSize = MutableStateFlow(0)
    val surveyItemSize = _surveyItemSize.asStateFlow()

    private val _submittedQuestions = MutableStateFlow(0)
    val submittedQuestions = _submittedQuestions.asStateFlow()

    fun fetchQuestions() {
        viewModelScope.launch {
            _surveyItemsUiState.emit(UiState.Loading())
            val response = repository.fetchQuestions()
            handleResponse(response)
        }
    }

    fun submitAnswer(surveyItem: SurveyItem) {
        viewModelScope.launch {
            _surveyItemsUiState.emit(UiState.Loading())
            val response = repository.submitAnswer(surveyItem)
            handleAnswerResponse(response)
        }
    }

    fun retryAnswer(surveyItem: SurveyItem) {
        viewModelScope.launch {
            _submittedQuestions.value = _submittedQuestions.value.minus(1)
            itemList.find { it.id == surveyItem.id }?.apply {
                answerText = ""
                answerResult = AnswerResult.NOT_SUBMITTED
            }
            _surveyItemsUiState.emit(UiState.Success(itemList))
        }
    }

    private fun handleAnswerResponse(response: Resource<SurveyItem>) {
        viewModelScope.launch {
            when (response) {
                is Resource.Success -> {
                    _submittedQuestions.value = _submittedQuestions.value.plus(1)
                    response.data?.let { submittedItem ->
                        itemList.find { submittedItem.id == it.id }?.apply {
                            answerText = submittedItem.answerText
                            answerResult = submittedItem.answerResult
                        }
                        _surveyItemsUiState.emit(UiState.Success(itemList))
                    }
                }

                is Resource.Error -> {
                    _surveyItemsUiState.emit(UiState.Error(response.exception))
                }
            }
        }
    }

    private fun handleResponse(response: Resource<List<QuestionDto>>) {
        viewModelScope.launch {
            when (response) {
                is Resource.Success -> {
                    response.data?.let { list ->
                        val newList = list.map { questionDto -> SurveyItem(questionDto) }
                        itemList.clear()
                        itemList.addAll(newList)
                        _surveyItemSize.value = itemList.size
                        _surveyItemsUiState.emit(UiState.Success(itemList))
                    }
                }

                is Resource.Error -> {
                    _surveyItemsUiState.emit(UiState.Error(response.exception))
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val repository: SurveyRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
                return SurveyViewModel(
                    repository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}