package com.app.cityshow.network

import com.app.cityshow.model.ObjectBaseModel
import com.app.cityshow.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(NetworkURL.LOGIN)
    suspend fun login(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>
/*

    @GET(NetworkURL.GET_PROFILE)
    suspend fun getProfile(): Response<ObjectBaseModel<User>>


    @Multipart
    @POST(NetworkURL.ASSET_REGISTRATION)
    suspend fun assetRegistration(
        @PartMap params: HashMap<String, RequestBody>,
        @Part photos: ArrayList<MultipartBody.Part>?,
    ): Response<BaseModel>

    @POST(NetworkURL.INVENTORY_DATA)
    suspend fun getInventories(@Body params: HashMap<String, Any>): Response<ListBaseModel<InventoryModel>>
*/

}