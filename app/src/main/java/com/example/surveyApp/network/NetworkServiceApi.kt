package com.example.surveyApp.network

import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.models.AnswerModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkServiceApi {

    @GET("/questions")
    suspend fun getQuestionsList() : Response<List<QuestionDto>>

    @POST("/question/submit")
    suspend fun submitAnswer(@Body answer: AnswerModel): Response<Unit>
}