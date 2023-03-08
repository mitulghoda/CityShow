package com.app.cityshow.model

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class AbstractCallback<T> : Callback<T> {
    private var message = ""
    abstract fun result(result: T?)
    override fun onResponse(call: Call<T>, response: Response<T>) {
        result(response.body())
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        message = t.localizedMessage
        result(null)
    }
}