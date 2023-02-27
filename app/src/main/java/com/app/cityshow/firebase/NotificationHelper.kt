package com.app.cityshow.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.cityshow.Controller
import com.app.cityshow.Controller.Companion.instance
import com.app.cityshow.R
import com.app.cityshow.ui.activity.HomeActivity

object NotificationHelper {
    var notificationManager: NotificationManager =
        instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private const val CHANNEL_ID = "cityshow_general_notification_channel_id"
    private const val CHANNEL_NAME = "cityshow_notification_channel"

    private const val iconBasedOnVersion: Int = R.drawable.ic_notification_logo
    val uniqueId: Int = (System.currentTimeMillis() and 0xfffffff).toInt()

    fun show(title: String?, body: String?, notificationId: Int = uniqueId) {
        createChannel()
        val context: Context = instance
        val messageBody = body
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).setStyle(
            NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(messageBody)
        ).setContentTitle(title).setContentText(messageBody)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setAutoCancel(true).setContentIntent(context.getCommonPendingIntent(notificationId))
        if (ActivityCompat.checkSelfPermission(
                instance,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }

    fun show(
        title: String?,
        body: String?,
        notificationId: Int,
        pendingIntent: PendingIntent,
    ) {
        val messageBody = body
        createChannel()
        val context: Context = instance
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).setStyle(
            NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(messageBody)
        ).setContentTitle(instance.getString(R.string.app_name)).setContentText(messageBody)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setColor(instance.getColor(R.color.colorAccent))
            .setAutoCancel(true).setContentIntent(pendingIntent)
        if (ActivityCompat.checkSelfPermission(
                instance,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
            NotificationManagerCompat.from(instance).createNotificationChannel(channel)
        }
    }

    fun cancel(notification_id: Int) = notificationManager.cancel(notification_id)

    fun cancel(tag: Long?, notification_id: Int) {
        val notificationManager = NotificationManagerCompat.from(instance)
        notificationManager.cancel(tag.toString(), notification_id)
        cancel(notification_id)
    }

    private fun Context.getCommonPendingIntent(uniqueId: Int): PendingIntent {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(
            this,
            uniqueId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}