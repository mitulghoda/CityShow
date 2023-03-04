package com.app.cityshow.model.subscription

data class Recurring(
    val aggregate_usage: String,
    val interval: String,
    val interval_count: Int,
    val trial_period_days: Any,
    val usage_type: String
)