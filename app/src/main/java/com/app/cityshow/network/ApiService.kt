package com.app.cityshow.network

import com.app.cityshow.model.BaseModel
import com.app.cityshow.model.LoginUserModel
import com.app.cityshow.model.ObjectBaseModel
import com.app.cityshow.model.User
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.model.shops.ShopsModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>

    @POST("logout")
    suspend fun logout(): Response<BaseModel>

    @POST("register")
    @Multipart
    suspend fun register(
        @PartMap params: HashMap<String, Any>?,
        @Part image: MultipartBody.Part?,
    ): Response<ObjectBaseModel<LoginUserModel>>

    @POST("send-forgot-password-otp")
    suspend fun sendForgotPassword(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>

    @POST("forgot-password")
    suspend fun changePassword(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>


    @GET("get_user_details")
    suspend fun getProfile(): Response<ObjectBaseModel<User>>

    @GET("categories/get-list")
    suspend fun getCategories(@QueryMap param: HashMap<String, Any>): Response<ObjectBaseModel<CategoryModel>>

    @GET("product/details/{id}")
    suspend fun getProductDetails(@Path("id") id: String): Response<ObjectBaseModel<User>>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<ObjectBaseModel<User>>

    @POST("product/update")
    suspend fun updateProduct(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>

    @POST("product/add-to-favourite")
    suspend fun productAddToFav(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>

    @POST("shops")
    suspend fun myShops(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<ShopsModel>>

    @Multipart
    @POST("user/update_profile")
    suspend fun updateProfile(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part image: MultipartBody.Part?,
    ): Response<ObjectBaseModel<User>>

    @Multipart
    @POST("product/create")
    suspend fun createProduct(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part images: ArrayList<MultipartBody.Part?>,
    ): Response<ObjectBaseModel<User>>

    @Multipart
    @POST("shop-add")
    suspend fun addEditShop(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part banner: MultipartBody.Part?,
        @Part images: ArrayList<MultipartBody.Part?>,
    ): Response<ObjectBaseModel<User>>


    @Multipart
    @POST(NetworkURL.ASSET_REGISTRATION)
    suspend fun assetRegistration(
        @PartMap params: HashMap<String, RequestBody>,
        @Part photos: ArrayList<MultipartBody.Part>?,
    ): Response<BaseModel>

//    @POST(NetworkURL.INVENTORY_DATA)
//    suspend fun getInventories(@Body params: HashMap<String, Any>): Response<ListBaseModel<InventoryModel>>

}