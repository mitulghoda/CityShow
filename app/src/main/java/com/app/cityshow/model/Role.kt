package com.app.cityshow.model
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Role(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("guard_name")
    var guardName: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("pivot")
    var pivot: Pivot?,
    @SerializedName("updated_at")
    var updatedAt: String?
)