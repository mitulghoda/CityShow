package com.app.cityshow.model.stripe

import com.app.cityshow.model.UserSubscription
import com.google.gson.annotations.SerializedName

data class GeneralModel(
    @SerializedName("data")
    val data: UserSubscription,
    val message: String,
    val status: Int,
    val success: Boolean,
)