package com.app.cityshow.model.shops

import com.app.cityshow.model.product.Product

data class Shop(
    val address: String,
    val banner: String,
    val banner_image: String,
    val category_id: String,
    val created_at: String,
    val deleted_at: String,
    val full_video: String,
    val id: String,
    val is_verified_admin: String,
    val latitude: String,
    val longitude: String,
    val notes: String,
    val shop_images: List<Any>,
    val products: ArrayList<Product> = ArrayList(),
    val shop_name: String,
    val status: String,
    val updated_at: String,
    val user_id: String,
    val video: String,
) : java.io.Serializable {
    fun getTotalProduct(): String {
        return "${products.size} products "
    }
}