package com.app.cityshow.model

data class UserSubscription(
    val amount: Int,
    val cancel_reason: Any,
    val created_at: String,
    val from_date: String,
    val id: Int,
    val is_cancelled: Int,
    val is_current_subscription: Int,
    val plan_stripe_id: String,
    val price_stripe_id: String,
    val stripe_subscription_id: String,
    val subscribe_cancel_id: Any,
    val subscription_id: Any,
    val to_date: String,
    val updated_at: String,
    val user_id: Int
)