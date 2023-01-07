package com.app.cityshow.model
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Pivot(
    @SerializedName("model_id")
    var modelId: String?,
    @SerializedName("model_type")
    var modelType: String?,
    @SerializedName("role_id")
    var roleId: String?
)