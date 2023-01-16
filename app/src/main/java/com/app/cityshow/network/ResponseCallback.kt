package com.app.cityshow.network

interface ResponseCallback<T> {
    fun onSuccess(response: T)
    fun onError(message: String, code: Int, t: Throwable? = null)
}