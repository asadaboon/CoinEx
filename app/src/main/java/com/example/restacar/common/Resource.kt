package com.example.restacar.common

sealed class Resource<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val message: String? = null
) {
    data class Loading<out R>(val _data: R?, val isLoading: Boolean) : Resource<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = null
    )

    data class Success<out R>(val _data: R?) : Resource<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String) : Resource<Nothing>(
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