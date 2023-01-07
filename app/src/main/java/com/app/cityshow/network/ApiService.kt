package com.app.cityshow.network

import com.app.cityshow.model.BaseModel
import com.app.cityshow.model.LoginUserModel
import com.app.cityshow.model.ObjectBaseModel
import com.app.cityshow.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>

    @POST("logout")
    suspend fun logout(): Response<BaseModel>

    @GET("get_user_details")
    suspend fun getProfile(): Response<ObjectBaseModel<User>>

    @Multipart
    @POST("user/update_profile")
    suspend fun updateProfile(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part image: MultipartBody.Part?
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