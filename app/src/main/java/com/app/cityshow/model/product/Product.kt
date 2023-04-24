package com.app.cityshow.model.product

import com.app.cityshow.model.MyItemImages
import com.app.cityshow.model.User
import com.app.cityshow.model.disocunt.Discount
import com.app.cityshow.model.shops.Shop
import com.app.cityshow.utility.fromJsonList
import com.google.gson.annotations.SerializedName

class Product : java.io.Serializable {
    val brand_name: String? = null
    val cat_id: String? = null
    val color: String? = null
    val connectivity: String? = null
    val created_at: String? = null
    val description: String? = null
    val device_os: String? = null
    val gender: String? = null
    val id: String? = null
    var is_fav: String? = ""
    var discount: Discount? = null
    val is_gold: String? = null
    val key_featurees: String? = null
    val material: String? = null
    val model_name: String? = null
    var name: String? = null
    var checked: Boolean = false
    val price: String? = null
    val product_image: ArrayList<MyItemImages>? = null
    val product_shop: ArrayList<Shop>? = null
    val shopkeeper: User? = null
    val ram: String? = null
    val selling_price: String? = null
    val shopkeeper_id: String? = null

    @SerializedName("size", alternate = ["footwear_size"])
    val size: String? = null
    val storage: String? = null
    val subcat_id: String? = null
    val updated_at: String? = null
    val wight: String? = null
    val warranty: String? = null
    val installation: String? = null
    val certified_jwellery: String? = null
    val guaranty: String? = null
    val emi: String? = null
    val shop: Shop? = null

    fun getProductImage(): String {
        return if (!product_image.isNullOrEmpty()) {
            product_image[0].image_url
        } else {
            ""
        }
    }

    fun isShowWarranty(): Boolean {
        if (is_gold.isNullOrEmpty().not()) {
            return true
        } else if (warranty.isNullOrEmpty().not()) {
            return true
        }
        return false
    }

    fun getShopImage(): String {
        return if (!product_shop.isNullOrEmpty()) {
            product_shop[0].banner_image
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
            product_shop[0].address + ",\n" + product_shop[0].city
        }
    }

    fun getShopPhoneNumber(): String {
        return shopkeeper?.phoneNumber ?: ""
    }

    fun hideDiscount(): Boolean {
        return discount?.id.isNullOrEmpty()
    }

    fun shoePhoneNumber(): Boolean {
        return shopkeeper?.phoneNumber.isNullOrEmpty()
    }

    fun getDiscountTitle(): String {
        if (discount != null) {
            return if (discount!!.is_price.equals("yes", true)) {
                "(Flat${discount!!.discount}off)"
            } else {
                "(Flat${discount!!.discount}%off)"
            }
        }
        return ""
    }

    fun getDiscountCode(): String {
        if (discount != null) {
            return "Use this coupon ${discount?.coupon_code ?: ""}"
        }
        return ""
    }

    fun getShopName(): String {
        return if (product_shop.isNullOrEmpty()) {
            ""
        } else {
            product_shop[0].shop_name
        }
    }

    fun getShopTime(): String {
        return "Shop time\nOpen at ${product_shop?.get(0)?.open_time} Close at ${
            product_shop?.get(
                0
            )?.closed_time
        }"
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
