package com.leaird.fetchem.model

import androidx.annotation.StringRes

sealed class Result<T> {
    class Loading<T> : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(@StringRes val message: Int = 0) : Result<T>()

    fun value(): T? = when (this) {
        is Success<T> -> this.data
        else -> null
    }
}