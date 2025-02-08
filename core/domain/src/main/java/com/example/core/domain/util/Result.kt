package com.example.core.domain.util

import com.example.core.domain.util.Error as IError

sealed interface Result<out D, out E: IError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E: IError>(val error: E) : Result<Nothing, E>
}

inline fun <T, E: IError, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: IError> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

typealias EmptyResult<E> = Result<Unit, E>