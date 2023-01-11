package com.app.cityshow.viewmodel

import androidx.lifecycle.*
import com.app.cityshow.network.Resource
import com.app.cityshow.network.ResponseHandler
import com.app.cityshow.network.ResponseHandler.responseParser
import com.app.cityshow.repository.UserRepository
import com.app.cityshow.utility.Log
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel : ViewModel() {

    fun login(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.login(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun register(param: HashMap<String, Any>, image: MultipartBody.Part?) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(UserRepository.register(param, image), this)
            } catch (e: Exception) {
                e.message?.let { Log.e(it) }
                emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun sendForgot(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.sendForgot(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun verifyOtp(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.verifyOtp(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun logout() = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.logout(), this)
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.getProfile(), this)
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun updateProfile(param: HashMap<String, RequestBody>, image: MultipartBody.Part?) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(UserRepository.updateProfile(param = param, image = image), this)
            } catch (e: Exception) {
                emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
            }
        }
}