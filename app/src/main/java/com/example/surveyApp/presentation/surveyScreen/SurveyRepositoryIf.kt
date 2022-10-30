package com.example.surveyApp.presentation.surveyScreen

import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.network.Resource

interface SurveyRepositoryIf {

    suspend fun fetchQuestions() : Resource<List<QuestionDto>>
    suspend fun submitAnswer(surveyItem: SurveyItem) : Resource<SurveyItem>
}