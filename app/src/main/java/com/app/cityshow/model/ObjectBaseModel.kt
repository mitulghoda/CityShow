package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ObjectBaseModel<T>(
    @SerializedName("Response", alternate = ["data"]) var data: T,
) :
    BaseModel(0, "Something went wrong")
