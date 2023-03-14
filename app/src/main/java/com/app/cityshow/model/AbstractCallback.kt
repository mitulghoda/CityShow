package com.app.cityshow.model

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class AbstractCallback<T> : Callback<T> {
    private var message = ""
    abstract fun result(result: T?)
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) result(response.body())
            else result(response.body())
        } else {
            message = response.message()
            result(null)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        message = t.localizedMessage
        result(null)
    }
}