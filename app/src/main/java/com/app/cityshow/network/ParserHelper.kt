package com.app.cityshow.network

import androidx.lifecycle.LiveDataScope
import com.app.cityshow.model.BaseModel
import com.app.cityshow.model.ErrorBaseModel
import com.app.cityshow.network.NetworkURL.RESPONSE_CREATED
import com.app.cityshow.network.NetworkURL.RESPONSE_OK
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.ResponseBody
import retrofit2.Response

object ParserHelper {
    fun baseError(error: ResponseBody?): BaseModel {
        return try {
            Gson().fromJson(error!!.charStream(), BaseModel::class.java)
        } catch (e: Exception) {
            BaseModel(0, ResponseHandler.handleErrorResponse(e))
        }
    }

    fun <T> baseError(response: Response<T>): BaseModel {
        return try {
            return if (response.code() == RESPONSE_OK || response.code() == RESPONSE_CREATED) {
                val baseModel = Gson().fromJson(response.body().toString(), BaseModel::class.java)
                baseModel.code = response.code()
                baseModel
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorBaseModel::class.java)

                val errorModel = BaseModel()
                errorModel.code = response.code()
                errorModel.message = error.getDataMessage()
                errorModel
            }
        } catch (e: Exception) {
            if (response.body() is BaseModel) response.body() as BaseModel
            else BaseModel(response.code(), ResponseHandler.handleErrorResponse(e))
        }
    }

    fun exceptionHandler(callback: ((errorMessage: String) -> Unit)? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            callback?.invoke(ResponseHandler.handleErrorResponse(throwable))
        }
    }


    suspend fun <T> responseParser(
        response: Response<T>,
        liveDataScope: LiveDataScope<Resource<T>>,
    ) {
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) liveDataScope.emit(Resource.success(result))
            else liveDataScope.emit(
                Resource.error(
                    data = null,
                    message = baseError(response).message
                )
            )
        } else {
            liveDataScope.emit(Resource.error(data = null, message = baseError(response).message))
        }
    }
}