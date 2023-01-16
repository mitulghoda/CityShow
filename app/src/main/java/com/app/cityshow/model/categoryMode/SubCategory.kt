package com.app.cityshow.model.categoryMode

data class SubCategory(
    val category_id: String,
    val created_at: String,
    val deleted_at: String,
    val id: String,
    val name: String,
    val slug: String,
    val sub_category_image: List<SubCategoryImage>,
    val updated_at: String
)