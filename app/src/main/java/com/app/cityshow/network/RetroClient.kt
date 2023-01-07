package com.app.cityshow.network

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.cityshow.BuildConfig
import com.app.cityshow.Controller
import com.app.cityshow.network.NetworkURL.ACTION_FOR_BIDDEN_RESPONSE
import com.app.cityshow.network.NetworkURL.ACTION_FOR_BLOCKED
import com.app.cityshow.network.NetworkURL.ACTION_FOR_INACTIVE_USER
import com.app.cityshow.network.NetworkURL.HEADER_AUTHORIZATION
import com.app.cityshow.network.NetworkURL.RES_BLOCKED
import com.app.cityshow.network.NetworkURL.RES_UNAUTHORISED
import com.app.cityshow.network.NetworkURL.RES_USER_INACTIVE
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Log
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object RetroClient {
    private var retrofit: Retrofit? = null
    private val interceptor: Interceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY.takeIf { BuildConfig.DEBUG }
                ?: HttpLoggingInterceptor.Level.NONE)
            return logging
        }

    private val builder = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        request.addHeader("accept", "application/json")
        if (LocalDataHelper.login) {
            Log.e("$HEADER_AUTHORIZATION: ${LocalDataHelper.authToken}")
            request.addHeader(HEADER_AUTHORIZATION, LocalDataHelper.authToken.orEmpty())
        }
        chain.proceed(request.build())
    }

//    private val apiTokenInterceptor = Interceptor { chain ->
//        var request = chain.request()
//        if (AppHelper.isLogin()) {
//            val url: HttpUrl = request.url.newBuilder()
//                .addQueryParameter(QUERY_AUTHORIZATION, AppHelper.getApiToken()).build()
//            request = request.newBuilder().url(url).build()
//        }
//        chain.proceed(request)
//    }

    private val forbiddenInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            RES_UNAUTHORISED -> manageForBiddenRequest()
            RES_BLOCKED -> manageBlockRequest()
            RES_USER_INACTIVE -> manageInactiveRequest()
        }
        response
    }

    private fun manageForBiddenRequest() {
        val intent = Intent(ACTION_FOR_BIDDEN_RESPONSE)
        LocalBroadcastManager.getInstance(Controller.instance).sendBroadcast(intent)
    }

    private fun manageInactiveRequest() {
        val intent = Intent(ACTION_FOR_INACTIVE_USER)
        LocalBroadcastManager.getInstance(Controller.instance).sendBroadcast(intent)
    }

    private fun manageBlockRequest() {
        val intent = Intent(ACTION_FOR_BLOCKED)
        LocalBroadcastManager.getInstance(Controller.instance).sendBroadcast(intent)
    }

    private val client =
        builder.addInterceptor(interceptor)
            .addInterceptor(headerInterceptor)
//            .addInterceptor(apiTokenInterceptor)
            .addInterceptor(forbiddenInterceptor)
            .build()

    private val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val apiService: ApiService = retrofitInstance.create(ApiService::class.java)
}