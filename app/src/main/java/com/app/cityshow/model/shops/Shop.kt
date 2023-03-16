package com.app.cityshow.model.shops

import com.app.cityshow.model.MyItemImages
import com.app.cityshow.model.product.Product

class Shop : java.io.Serializable {
    var checked: Boolean = false
    val address: String = ""
    val city: String = ""
    val phone_number: String = ""
    val banner: String = ""
    val banner_image: String = ""
    val category_id: String = ""
    val created_at: String = ""
    val deleted_at: String = ""
    val full_video: String = ""
    val id: String = ""
    val is_verified_admin: String = ""
    val latitude: String = "0.0"
    val longitude: String = "0.0"
    val notes: String = ""
    val shop_images: List<MyItemImages> = ArrayList()
    val products: ArrayList<Product> = ArrayList()
    var shop_name: String = ""
    val status: String = ""
    val updated_at: String = ""
    val user_id: String = ""
    val video: String = ""
    fun getTotalProduct(): String {
        return "${products.size} products "
    }

    fun getShopAddress(): String {
        return "$address,$city"
    }
}
