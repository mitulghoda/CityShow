package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginUserModel(
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("token")
    var token: String = ""
)