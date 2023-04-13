package com.karyna.core.data

sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val throwable: Throwable? = null) : Result<T>()
}
