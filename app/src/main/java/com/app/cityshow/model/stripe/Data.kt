package com.app.cityshow.model.stripe

data class Data(
    val amount: String,
    val cancel_reason: String,
    val created_at: String,
    val from_date: String,
    val id: String,
    val is_cancelled: String,
    val is_current_subscription: String,
    val plan_stripe_id: String,
    val price_stripe_id: String,
    val stripe_subscription_id: String,
    val subscribe_cancel_id: String,
    val subscription_id: String,
    val to_date: String,
    val updated_at: String,
    val user_id: String
)