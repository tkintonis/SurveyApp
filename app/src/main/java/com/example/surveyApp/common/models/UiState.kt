package com.example.surveyApp.common.models

sealed class UiState<out T>{
    data class Success<T>(val data: T?) : UiState<T>()
    data class Loading(val msg: String = "") : UiState<Nothing>()
    data class Error(val exception: Exception) : UiState<Nothing>()
}
