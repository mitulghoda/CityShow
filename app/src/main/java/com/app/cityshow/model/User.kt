package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class User(
    @SerializedName("address")
    var address: String?,
    @SerializedName("birth_date")
    var birthDate: String?,
    @SerializedName("country_code")
    var countryCode: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("deleted_at")
    var deletedAt: String?,
    @SerializedName("device_token")
    var deviceToken: String?,
    @SerializedName("device_type")
    var deviceType: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("lastname")
    var lastname: String?,
    @SerializedName("otp")
    var otp: String?,
    @SerializedName("phone_number")
    var phoneNumber: String?,
    var full_profile_image: String?,
    @SerializedName("role")
    var role: List<Role?>?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("surname")
    var surname: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("stripe_customer_id")
    var stripeCustomerId: String?,
)