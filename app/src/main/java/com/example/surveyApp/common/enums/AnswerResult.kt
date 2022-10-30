package com.example.surveyApp.common.enums

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.surveyApp.R
import com.example.surveyApp.common.constants.Constants.FAIL_STATUS_CODE
import com.example.surveyApp.common.constants.Constants.SUCCESS_STATUS_CODE

enum class AnswerResult(
    private val code: Int,
    val editTextVisibility: Int,
    val answerVisibility: Int,
    val popupVisibility: Int,
    val retryButtonVisibility: Int,
    @ColorRes val popUpBackgroundColour: Int,
    @StringRes val popUpText: Int,
    @StringRes val submitButtonText: Int,
    @ColorRes val submitButtonTextColor: Int,
    var submitButtonClickable: Boolean
) {
    SUCCESS(
        SUCCESS_STATUS_CODE,
        View.GONE,
        View.VISIBLE,
        View.VISIBLE,
        View.GONE,
        R.color.zeus,
        R.string.success,
        R.string.submitted,
        R.color.g400,
        false
    ),
    FAIL(
        FAIL_STATUS_CODE,
        View.GONE,
        View.VISIBLE,
        View.VISIBLE,
        View.VISIBLE,
        R.color.marsDark,
        R.string.failure,
        R.string.submitted,
        R.color.g400,
        false
    ),
    NOT_SUBMITTED(0, View.VISIBLE, View.GONE, View.GONE, View.GONE, R.color.g400, R.string.submit, R.string.submit, R.color.light_blue, true);

    companion object {
        private val map = values().associateBy(AnswerResult::code)
        fun create(type: Int?): AnswerResult = map[type] ?: NOT_SUBMITTED
    }
}