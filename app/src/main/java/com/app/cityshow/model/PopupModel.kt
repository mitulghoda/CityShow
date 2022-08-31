package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class PopupModel(
    @SerializedName("id", alternate = ["LocationId", "AssetTypeId", "AssetCategoryId"])
    val id: String,

    @SerializedName("value", alternate = ["Location", "AssetType", "AssetCategory"])
    val value: String,
    val LocationCode: String?=null,
):Serializable
