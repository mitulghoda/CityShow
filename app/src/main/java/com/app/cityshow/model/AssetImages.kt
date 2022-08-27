package com.app.cityshow.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
class AssetImages(
    @SerializedName("AssetImage")
    var assetImage: String,
)