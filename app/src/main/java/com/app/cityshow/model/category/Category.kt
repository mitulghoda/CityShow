package com.app.cityshow.model.category

import com.app.cityshow.model.shops.Shop

data class Category(
    val created_at: String,
    val deleted_at: String,
    val id: String,
    val name: String,
    val slug: String,
    val sub_category: List<SubCategory>,
    val category_images: List<String>,
    val updated_at: String,
    val shops: List<Shop>,
):java.io.Serializable {
    fun getTotalShop(): String {
        return "${shops.size}+shops"
    }

    fun getCategoryImage(): String {
        return if (category_images.isNotEmpty()) {
            category_images[category_images.size - 1]
        } else {
            ""
        }
    }
}