package com.app.cityshow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.cityshow.network.ParserHelper.responseParser
import com.app.cityshow.network.Resource
import com.app.cityshow.network.ResponseHandler
import com.app.cityshow.repository.ProductRepository
import com.app.cityshow.utility.Log
import com.app.cityshow.utility.gson
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

    fun subscribeUser(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.subscribeUser(param), this)
        } catch (e: Exception) {
            e.message?.let { Log.e(it) }
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun getDiscountedProduct(param: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getDiscountedProduct(param), this)
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

    fun deleteDiscount(param: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.deleteDiscount(param), this)
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

    fun myProduct(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.myProduct(param), this)
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

    fun getNotifications(params: HashMap<String, Any>?) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getNotifications(params), this)
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

    fun myDiscounts(param: HashMap<String, Any>) = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.myDiscounts(param), this)
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun getSubscriptionsPlans() = liveData(Dispatchers.IO) {
        try {
            responseParser(ProductRepository.getSubscriptionsPlans(), this)
        } catch (e: Exception) {
            Log.e("Exception", e.message ?: "")
            emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
        }
    }


    fun createProduct(
        param: HashMap<String, RequestBody>,
        images: ArrayList<MultipartBody.Part?>? = null,
    ) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(
                    ProductRepository.createProduct(param = param, images = images),
                    this
                )
            } catch (e: Exception) {
                Log.e(gson.toJson(e))
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun updateProduct(param: HashMap<String, RequestBody>, images: ArrayList<MultipartBody.Part?>) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(
                    ProductRepository.updateProduct(param = param, images = images),
                    this
                )
            } catch (e: Exception) {
                Log.e(e.message ?: "")
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
                responseParser(
                    ProductRepository.addEditShop(
                        param = param,
                        banner = banner,
                        images = images
                    ), this
                )
            } catch (e: Exception) {
                Log.e("Exception", e.message ?: "")
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }

    fun addEditDiscount(
        param: HashMap<String, RequestBody>,
        banner: MultipartBody.Part?,
        images: ArrayList<MultipartBody.Part?>,
    ) =
        liveData(Dispatchers.IO) {
            try {
                responseParser(
                    ProductRepository.addEditDiscount(
                        param = param,
                        banner = banner,
                        images = images
                    ), this
                )
            } catch (e: Exception) {
                Log.e(e.message ?: "")
                emit(Resource.error(data = null, message = ResponseHandler.handleErrorResponse(e)))
            }
        }
}