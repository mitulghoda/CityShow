package com.app.cityshow.network

data class Resource<out T>(val status: ApiStatus, val data: T? = null, val message: String = "") {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(ApiStatus.SUCCESS, data)
        fun <T> error(message: String): Resource<T> = Resource(ApiStatus.ERROR, message = message)
//        fun <T> error(data: T?, message: String): Resource<T> = Resource(ApiStatus.ERROR, data, message)
//        fun <T> loading(data: T?): Resource<T> = Resource(ApiStatus.LOADING, data)
    }
}