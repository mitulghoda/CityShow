package com.app.cityshow.model.disocunt

import com.app.cityshow.utility.DateTimeUtil

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
    val updated_at: String,
) : java.io.Serializable {
    fun getDiscountDate(): String {
        val startDate = DateTimeUtil.formatDate(
            "yyyy-MM-dd hh:mm:ss",
            "dd MMM, yyyy",
            start_date
        )
        val endDate = DateTimeUtil.formatDate(
            "yyyy-MM-dd hh:mm:ss",
            "dd MMM, yyyy",
            start_date
        )
        return "Start-$startDate to End-$endDate"
    }

    fun getStartDate(): String {
//        "2023-03-31 00:00:00"
        return  DateTimeUtil.formatDate(
            "yyyy-MM-dd hh:mm:ss",
            "dd MMM, yyyy",
            start_date
        )
    }

    fun getEndDate(): String {
//        "2023-03-31 00:00:00"
        return  DateTimeUtil.formatDate(
            "yyyy-MM-dd hh:mm:ss",
            "dd MMM, yyyy",
            end_date
        )
    }
}