package com.app.cityshow.viewmodel

import androidx.lifecycle.*
import com.app.cityshow.network.ParserHelper.responseParser
import com.app.cityshow.network.Resource
import com.app.cityshow.network.ResponseHandler
import com.app.cityshow.repository.ProductRepository
import com.app.cityshow.utility.Log
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductViewModel : ViewModel() {

    fun getProductDetails(param: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getProductDetails(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun deleteProduct(param: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.deleteProduct(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun updateProduct(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.updateProduct(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun productAddToFav(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.productAddToFav(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }
   fun listOfProduct(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.listOfProduct(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }
   fun getFavProduct() = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getFavProduct(), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun getCategories(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getCategories(param), this)
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }
    fun markFav(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.markFavProduct(param), this)
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun myShops(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.myShops(param), this)
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }


    fun createProduct(param: HashMap<String, RequestBody>, images: ArrayList<MultipartBody.Part?>) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(ProductRepository.createProduct(param = param, images = images),
                    this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun addEditShop(
        param: HashMap<String, RequestBody>,
        banner: MultipartBody.Part?,
        images: ArrayList<MultipartBody.Part?>,
    ) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(ProductRepository.addEditShop(param = param,
                    banner = banner,
                    images = images), this)
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }
}