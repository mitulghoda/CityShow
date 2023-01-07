package com.app.cityshow.repository

import com.app.cityshow.network.RetroClient.apiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

object UserRepository {

    suspend fun login(param: HashMap<String, Any>) = apiService.login(param)

    suspend fun logout() = apiService.logout()

    suspend fun getProfile() = apiService.getProfile()

    suspend fun updateProfile(param: HashMap<String, RequestBody>, image: MultipartBody.Part?) =
        apiService.updateProfile(param, image)
}