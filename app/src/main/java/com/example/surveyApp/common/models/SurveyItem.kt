package com.example.surveyApp.common.models

import com.example.surveyApp.common.dto.QuestionDto
import com.example.surveyApp.common.enums.AnswerResult

data class SurveyItem(
    val id: Int = -1,
    val question: String = "",
    var answerText: String = "",
    var answerResult: AnswerResult = AnswerResult.NOT_SUBMITTED
) {

    constructor(questionDto: QuestionDto) : this(
        id = questionDto.id,
        question = questionDto.question
    )
}
