package com.app.cityshow.model.disocunt

import com.app.cityshow.model.product.Product

data class DiscountProduct(
    val coupon_code: String,
    val coupon_name: String,
    val created_at: String,
    val discount: String,
    val end_date: String,
    val id: String,
    val image: String,
    val is_price: String,
    val notes: String,
    val shop_keeper_id: String,
    val start_date: String,
    val updated_at: String,
    val discount_products: List<Product>,
)