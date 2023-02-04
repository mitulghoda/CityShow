package com.app.cityshow.model.disocunt

data class DiscountListModel(
    val discounts: List<Discount>,
    val total_counts: String,
    val total_pages: String
):java.io.Serializable