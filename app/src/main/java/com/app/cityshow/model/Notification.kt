package com.app.cityshow.model

data class Notification(
    val created_at: String,
    val deleted_at: String,
    val discount_id: String,
    val icon_type: String,
    val id: String,
    val message: String,
    val product_id: String,
    val send_by: String,
    val title: String,
    val type: String,
    val updated_at: String
)