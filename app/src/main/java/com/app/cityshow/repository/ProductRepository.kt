package com.app.cityshow.repository

import com.app.cityshow.network.RetroClient.apiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

object ProductRepository {

    suspend fun getProductDetails(param: String) = apiService.getProductDetails(param)
    suspend fun deleteProduct(param: String) = apiService.deleteProduct(param)

    suspend fun updateProduct(param: HashMap<String, Any>) = apiService.updateProduct(param)
    suspend fun productAddToFav(param: HashMap<String, Any>) = apiService.productAddToFav(param)

    suspend fun getCategories(param: HashMap<String, Any>) = apiService.getCategories(param)

    suspend fun createProduct(param: HashMap<String, RequestBody>, image: MultipartBody.Part?) =
        apiService.createProduct(param, image)
}