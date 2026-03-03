package com.example.journeytowealth.core.result

sealed class HttpResult<out T> {
    data class Success<out T>(val data: T) : HttpResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : HttpResult<Nothing>()
    object Loading : HttpResult<Nothing>()
}