package com.example.surveyApp.common.dto

import com.google.gson.annotations.SerializedName

data class QuestionDto(
    @SerializedName("id") private val _id: Int? = -1,
    @SerializedName("question") private val _question: String? = ""
) {

    val id: Int
        get() = _id ?: -1

    val question: String
        get() = _question ?: ""
}
