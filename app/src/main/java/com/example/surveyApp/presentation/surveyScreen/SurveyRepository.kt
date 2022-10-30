package com.example.surveyApp.presentation.surveyScreen

import android.content.res.Resources
import android.net.ConnectivityManager
import com.example.surveyApp.R
import com.example.surveyApp.common.helpers.DispatcherProvider
import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.enums.AnswerResult
import com.example.surveyApp.common.models.AnswerModel
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.network.NetworkServiceApi
import com.example.surveyApp.network.Resource
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class SurveyRepository @Inject constructor(
    private val api: NetworkServiceApi,
    private val connectivityManager: ConnectivityManager,
    private val dispatcherProvider: DispatcherProvider,
    private val resources: Resources
) : SurveyRepositoryIf {

    companion object {
        private const val TIMEOUT_THRESHOLD_MILLIS = Long.MAX_VALUE
    }

    override suspend fun fetchQuestions(): Resource<List<QuestionDto>> {
        if (!hasInternetConnectivity())
            return Resource.Error(Exception(resources.getString(R.string.internet_connection_message)))

        return withContext(dispatcherProvider.ioDispatcher) {
            try {
                withTimeout(TIMEOUT_THRESHOLD_MILLIS) {
                    val response = api.getQuestionsList()
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        Resource.Success(result)
                    } else {
                        Resource.Error(Exception(response.message()))
                    }
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    override suspend fun submitAnswer(surveyItem: SurveyItem): Resource<SurveyItem> {
        if (!hasInternetConnectivity())
            return Resource.Error(Exception(resources.getString(R.string.internet_connection_message)))

        return withContext(dispatcherProvider.ioDispatcher) {
            try {
                withTimeout(TIMEOUT_THRESHOLD_MILLIS) {
                    val postBody = AnswerModel(surveyItem.id, surveyItem.answerText)
                    val response = api.submitAnswer(postBody)
                    if (response.isSuccessful) {
                        Resource.Success(surveyItem.apply {
                            answerResult = AnswerResult.create(response.code())
                        })
                    } else {
                        Resource.Success(surveyItem.apply {
                            answerResult = AnswerResult.create(response.code())
                        })
                    }
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    private fun hasInternetConnectivity(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}