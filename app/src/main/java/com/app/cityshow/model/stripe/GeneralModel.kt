package com.app.cityshow.model.stripe

import com.google.gson.annotations.SerializedName

data class GeneralModel(
    @SerializedName("data")
    val data: Data,
    val message: String,
    val status: Int,
    val success: Boolean,
)