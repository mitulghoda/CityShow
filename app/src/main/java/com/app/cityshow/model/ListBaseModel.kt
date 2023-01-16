package com.app.cityshow.model

import com.google.gson.annotations.SerializedName

data class ListBaseModel<T>(
    @SerializedName("data")
    var data: List<T>,
) : BaseModel(0, "Something went wrong")