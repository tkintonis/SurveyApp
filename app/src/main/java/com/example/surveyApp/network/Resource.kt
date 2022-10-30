package com.example.surveyApp.network

sealed class Resource<T> {

    class Success<T>(val data: T?) : Resource<T>()
    class Error<T>(val exception: Exception) : Resource<T>()
}