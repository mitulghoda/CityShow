package com.app.cityshow.firebase

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.cityshow.Controller.Companion.instance
import com.app.cityshow.firebase.NotificationHelper.show
import com.app.cityshow.firebase.NotificationHelper.uniqueId
import com.app.cityshow.ui.activity.HomeActivity
import com.app.cityshow.utility.LocalDataHelper
import com.app.cityshow.utility.Utils
import com.app.cityshow.utility.getInt
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlin.collections.set

class FirebaseNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "onMessageReceived")
        Log.e(TAG, Gson().toJson(remoteMessage.notification))
        if (remoteMessage.data.isEmpty()) return
        val param: Map<String, String> = remoteMessage.data
        val jsonData = Gson().toJson(param)
        Log.e(TAG, "Notification Data $jsonData")
        //if (!LocalDataHelper.isLogin()) return

        val title = param["title"]
        val body = param["body"]
        val type = param["type"] ?: TYPE_GENERAL
        manageNotification(title, body, type, param)
    }

    private fun manageNotification(
        title: String?,
        body: String?,
        type: String,
        param: Map<String, String>,
    ) {
        val notificationId = uniqueId
        when (type) {
            TYPE_GENERAL -> {
                val exchangeIntent = Intent(instance, HomeActivity::class.java)
                show(
                    title,
                    body,
                    notificationId,
                    getPendingIntent(this.applicationContext, notificationId, exchangeIntent)
                )
            }
            else -> {
                show(title, body)
            }
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM Token", token)
        if (!LocalDataHelper.login) return
        updateToken(token)
    }

    companion object {
        private val TAG = FirebaseNotificationService::class.java.simpleName
        const val TYPE_GENERAL = "general"

        fun updateToken(token: String) {
            val params = HashMap<String, Any>()
            params["notificationToken"] = token
            params["deviceType"] = "Android"
            params["deviceId"] = Utils.getUniquePsuedoID()
        }
    }

    private fun getPendingIntent(context: Context, uniqueId: Int, intent: Intent): PendingIntent {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        Log.e("type", intent.getInt("REQUEST_KEY").toString())
        return PendingIntent.getActivity(
            context,
            uniqueId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}