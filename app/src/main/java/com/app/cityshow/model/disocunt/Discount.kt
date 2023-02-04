package com.app.cityshow.model.disocunt

data class Discount(
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
    val updated_at: String
)