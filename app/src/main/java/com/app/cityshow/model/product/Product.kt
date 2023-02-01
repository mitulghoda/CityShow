package com.app.cityshow.model.product

import com.app.cityshow.model.MyItemImages
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.utility.fromJsonList
import com.app.cityshow.utility.justTry

class Product() : java.io.Serializable {
    val brand_name: String = ""
    val cat_id: String = ""
    val color: String = ""
    val connectivity: String = ""
    val created_at: String = ""
    val description: String = ""
    val device_os: String = ""
    val gender: String = ""
    val id: String? = null
    var is_fav: String? = ""
    val is_gold: String = ""
    val key_featurees: String = ""
    val material: String = ""
    val model_name: String = ""
    var name: String = ""
    val price: String = ""
    val product_image: ArrayList<MyItemImages>? = null
    val product_shop: ArrayList<Shop>? = null
    val ram: String = ""
    val selling_price: String = ""
    val shopkeeper_id: String = ""
    val size: String = ""
    val storage: String = ""
    val subcat_id: String = ""
    val updated_at: String = ""
    val wight: String = ""
    val warranty: String = ""
    val shop: Shop? = null

    fun getProductImage(): String {
        return if (!product_image.isNullOrEmpty()) {
            product_image[0].image_url
        } else {
            ""
        }
    }

    fun isFavourite(): Boolean {
        return is_fav.equals("1", true)
    }

    fun isGold(): String {
        return "Yes".takeIf { is_gold.equals("1", true) } ?: "No"
    }

    fun getShopAddress(): String {
        return if (product_shop.isNullOrEmpty()) {
            ""
        } else {
            product_shop[0].address
        }
    }

    fun getShopName(): String {
        return if (product_shop.isNullOrEmpty()) {
            ""
        } else {
            product_shop[0].shop_name
        }
    }

    fun isWarranty(): String {
        return "Yes".takeIf { warranty.equals("1", true) } ?: "No"
    }

    fun getKeyFeatures(): ArrayList<String> {
        return try {
            key_featurees.fromJsonList()
        } catch (e: Throwable) {
            e.printStackTrace()
            arrayListOf()
        }
    }
}
