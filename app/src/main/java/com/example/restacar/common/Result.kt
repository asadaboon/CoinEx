package com.example.restacar.common

sealed class Result<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val message: String? = null
) {
    data class Loading<out R>(val _data: R?, val isLoading: Boolean) : Result<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = null
    )

    data class Success<out R>(val _data: R?) : Result<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String) : Result<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}