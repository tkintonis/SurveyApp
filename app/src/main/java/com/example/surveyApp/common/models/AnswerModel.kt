package com.example.surveyApp.common.models

import com.google.gson.annotations.SerializedName

data class AnswerModel(
    @SerializedName("id") private val id: Int = -1,
    @SerializedName("answer") private val answer: String = ""
)