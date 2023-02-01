package com.app.cityshow.utility

import android.content.Context
import android.content.SharedPreferences
import com.app.cityshow.Controller
import com.app.cityshow.model.User

object LocalDataHelper {
    private var preference = Controller.instance.getSharedPreferences("Nada-Teach-RFID", Context.MODE_PRIVATE)

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

    var fcmToken: String?
        get() = preference.getString("fcmToken", "")
        set(value) = preference.edit { it.putString("fcmToken", value) }

    var user: User?
        get() = preference.getString("user_details", "").fromJson(User::class.java)
        set(value) = preference.edit { it.putString("user_details", value.toJson()) }

    val userId: String get() = user?.id.orEmpty()

    var authToken: String?
        get() = preference.getString("auth_token", "")
        set(value) = preference.edit { it.putString("auth_token", value) }

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
