package com.app.cityshow.repository

import com.app.cityshow.network.RetroClient.apiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

object ProductRepository {

    suspend fun getProductDetails(param: String) = apiService.getProductDetails(param)
    suspend fun deleteProduct(param: String) = apiService.deleteProduct(param)

    suspend fun updateProduct(param: HashMap<String, Any>) = apiService.updateProduct(param)
    suspend fun productAddToFav(param: HashMap<String, Any>) = apiService.productAddToFav(param)
    suspend fun listOfProduct(param: HashMap<String, Any>) = apiService.listOfProduct(param)
    suspend fun myProduct(param: HashMap<String, Any>) = apiService.myProduct(param)
    suspend fun getFavProduct() = apiService.getFavProduct()

    suspend fun getCategories(param: HashMap<String, Any>) = apiService.getCategories(param)
    suspend fun markFavProduct(param: HashMap<String, Any>) = apiService.markFavProduct(param)
    suspend fun myShops(param: HashMap<String, Any>) = apiService.myShops(param)
    suspend fun myDiscounts(param: HashMap<String, Any>) = apiService.myDiscounts(param)

    suspend fun createProduct(
        param: HashMap<String, RequestBody>,
        images: ArrayList<MultipartBody.Part?>,
    ) =
        apiService.createProduct(param, images)

    suspend fun addEditShop(
        param: HashMap<String, RequestBody>,
        banner: MultipartBody.Part?,
        images: ArrayList<MultipartBody.Part?>,
    ) =
        apiService.addEditShop(param, banner, images)

    suspend fun addEditDiscount(
        param: HashMap<String, RequestBody>,
        banner: MultipartBody.Part?,
        images: ArrayList<MultipartBody.Part?>,
    ) =
        apiService.addEditDiscount(param, banner, images)
}