package com.app.cityshow.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FootwearSizeModel(
    @SerializedName("name", alternate = ["categoryName"])
    var name: String? = "",
    var isCheck: Boolean = false
) {
}