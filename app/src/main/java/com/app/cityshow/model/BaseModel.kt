package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class BaseModel(@SerializedName("status") var code: Int = 0, @SerializedName("message") var message: String = "") {
    @SerializedName("success")
    val success: Boolean = false
    val token: String = ""
}