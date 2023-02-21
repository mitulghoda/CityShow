package com.app.cityshow.model.dynamicview

data class CategoryWiseViewModelItem(
    val category: String,
    val required_details: List<RequiredDetail>
)