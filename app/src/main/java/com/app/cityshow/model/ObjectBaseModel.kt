package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ObjectBaseModel<T>(
    @SerializedName("data") var data: T,
) : BaseModel(0, "Something went wrong")
