package com.app.cityshow.model.product

import com.app.cityshow.model.MyItemImages
import com.app.cityshow.model.shops.Shop

class Product() : java.io.Serializable {
    val brand_name: String = ""
    val cat_id: String = ""
    val color: String = ""
    val connectivity: String = ""
    val created_at: String = ""
    val description: String = ""
    val device_os: String = ""
    val gender: String = ""
    val id: Int? = null
    val is_gold: String = ""
    val key_featurees: String = ""
    val material: String = ""
    val model_name: String = ""
    val name: String = ""
    val price: String = ""
    val product_images: ArrayList<MyItemImages>? = null
    val product_shops: ArrayList<Shop>? = null
    val ram: String = ""
    val selling_price: String = ""
    val shopkeeper_id: String = ""
    val size: String = ""
    val storage: String = ""
    val subcat_id: String = ""
    val updated_at: String = ""
    val wight: String = ""
    val shop: Shop? = null

    fun getCategoryImage(): String {
        return if (!product_images.isNullOrEmpty()) {
            product_images[0].image_url
        } else {
            ""
        }
    }
}