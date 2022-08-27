package com.app.cityshow.utility

import android.content.Context
import android.content.SharedPreferences
import com.app.cityshow.model.User
import com.app.cityshow.utility.fromJson
import com.app.cityshow.utility.toJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class LocalDataHelper constructor(context: Context) {
    private var preference = context.getSharedPreferences("Nada-Teach-RFID", Context.MODE_PRIVATE)

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun clearPreference() {
        login = false
        user = null

        val editor = preference.edit()
        editor.clear()
//        editor.remove(JwtToken)
//        editor.remove(UserData)
        editor.apply()
    }

    var login: Boolean
        get() = preference.getBoolean("is_login", false)
        set(value) = preference.edit { it.putBoolean("is_login", value) }

    var apiToken: String?
        get() = preference.getString("api_token", "")
        set(value) = preference.edit { it.putString("api_token", value) }

    var user: User?
        get() = preference.getString("user_details", "").fromJson(User::class.java)
        set(value) = preference.edit { it.putString("user_details", value.toJson()) }

    val userId: String get() = user?.UserId.orEmpty()

    var fcmToken: String?
        get() = preference.getString("fcm_token", "")
        set(value) = preference.edit { it.putString("fcm_token", value) }

  /*  var locations: List<PopupModel>
        get() {
            return Gson().fromJson(preference.getString("locations", ""),
                object : TypeToken<List<PopupModel>>() {}.type) ?: emptyList()
        }
        set(value) = preference.edit { it.putString("locations", value.toJson()) }

    var partList: List<PartModel>
        get() {
            return Gson().fromJson(preference.getString("partList", ""),
                object : TypeToken<List<PartModel>>() {}.type) ?: emptyList()
        }
        set(value) = preference.edit { it.putString("partList", value.toJson()) }

    var assetType: List<PopupModel>
        get() {
            return Gson().fromJson(preference.getString("assetType", ""),
                object : TypeToken<List<PopupModel>>() {}.type) ?: emptyList()
        }
        set(value) = preference.edit { it.putString("assetType", value.toJson()) }*/

}
