package com.app.cityshow.network

import com.app.cityshow.model.*
import com.app.cityshow.model.category.CategoryModel
import com.app.cityshow.model.disocunt.DiscountListModel
import com.app.cityshow.model.disocunt.DiscountProduct
import com.app.cityshow.model.product.Product
import com.app.cityshow.model.product.ProductMainModel
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
        @PartMap params: HashMap<String, RequestBody>?,
        @Part image: MultipartBody.Part?,
    ): Response<ObjectBaseModel<LoginUserModel>>

    @POST("send-forgot-password-otp")
    suspend fun sendForgotPassword(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>

    @POST("forgot-password")
    suspend fun changePassword(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<User>>

    @Multipart
    @POST("update-profile")
    suspend fun updateProfile(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part image: MultipartBody.Part?,
    ): Response<ObjectBaseModel<User>>

    @GET("get_user_details")
    suspend fun getProfile(): Response<ObjectBaseModel<User>>

    @GET("categories/get-list")
    suspend fun getCategories(@QueryMap param: HashMap<String, Any>): Response<ObjectBaseModel<CategoryModel>>

    @POST("product/add-to-favourite")
    suspend fun markFavProduct(@Body param: HashMap<String, Any>): Response<ListBaseModel<CategoryModel>>

    @GET("product/get-favourite-list")
    suspend fun getFavProduct(): Response<ObjectBaseModel<ProductMainModel>>

    @GET("product/details/{id}")
    suspend fun getProductDetails(@Path("id") id: String): Response<ObjectBaseModel<Product>>

    @GET("discount/details/{id}")
    suspend fun getDiscountedProduct(@Path("id") id: String): Response<ObjectBaseModel<DiscountProduct>>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<ListBaseModel<User>>
    @DELETE("discount/{id}")
    suspend fun deleteDiscount(@Path("id") id: String): Response<ListBaseModel<User>>

    @POST("product/update")
    suspend fun updateProduct(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>


    @POST("product/add-to-favourite")
    suspend fun productAddToFav(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<LoginUserModel>>

    @POST("product/list")
    suspend fun listOfProduct(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<ProductMainModel>>

    @POST("product/list")
    suspend fun myProduct(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<ProductMainModel>>

    @POST("shops")
    suspend fun myShops(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<ShopsModel>>

    @POST("discount/list")
    suspend fun myDiscounts(@Body params: HashMap<String, Any>?): Response<ObjectBaseModel<DiscountListModel>>

    @Multipart
    @POST("product/create")
    suspend fun createProduct(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part images: ArrayList<MultipartBody.Part?>? = null,
    ): Response<ObjectBaseModel<Product>>

    @Multipart
    @POST("product/update")
    suspend fun updateProduct(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part images: ArrayList<MultipartBody.Part?>,
    ): Response<ObjectBaseModel<Product>>

    @Multipart
    @POST("shop-add")
    suspend fun addEditShop(
        @PartMap params: HashMap<String, RequestBody>?,
        @Part banner: MultipartBody.Part?,
        @Part images: ArrayList<MultipartBody.Part?>,
    ): Response<ObjectBaseModel<User>>

    @Multipart
    @POST("discount/create")
    suspend fun addEditDiscount(
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