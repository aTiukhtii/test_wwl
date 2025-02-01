package com.wwl.test.data.helper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

fun <T> Response<T>.handleApiResponse() = if (isSuccessful) {
    Result.success(body()!!)
} else Result.failure(Exception(message()))

suspend fun <T> asyncTryCatch(callback: suspend () -> Result<T>) = withContext(Dispatchers.IO) {
    try { callback() }
    catch (e: Exception) { Result.failure(e) }
}