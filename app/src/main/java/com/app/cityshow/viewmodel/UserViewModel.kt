package com.app.cityshow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.cityshow.network.ParserHelper.responseParser
import com.app.cityshow.network.Resource
import com.app.cityshow.network.ResponseHandler
import com.app.cityshow.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel : ViewModel() {
    fun login(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            responseParser(UserRepository.login(param), this)
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun register(param: HashMap<String, RequestBody>, image: MultipartBody.Part?) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.register(param, image), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun sendForgot(param: HashMap<String, Any>) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.sendForgot(param), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun verifyOtp(param: HashMap<String, Any>) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.verifyOtp(param), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun changePassword(param: HashMap<String, Any>) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.changePassword(param), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }


    fun logout() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.logout(), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }


    fun getProfile() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.getProfile(), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun updateProfile(param: HashMap<String, RequestBody>, image: MultipartBody.Part?) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                responseParser(UserRepository.updateProfile(param, image), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }
}