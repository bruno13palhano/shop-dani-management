package com.bruno13palhano.shopdanimanagement.ui.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

private const val PRIMARY_CHANNEL_ID = "shop_dani_notification_channel"
private const val PRIMARY_CHANNEL_NAME = "shop_dani_management_system"
private const val PRIMARY_CHANNEL_DESCRIPTION = "notifies expired products"

class ExpiredProductsNotification(
    private val notificationManager: NotificationManager,
    private val alarmManager: AlarmManager
) {
    fun setAlarmManager(
        notifyPendingIntent: PendingIntent,
        date: Long
    ) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, date, notifyPendingIntent)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(
                PRIMARY_CHANNEL_ID,
                PRIMARY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)
        notificationChannel.description = PRIMARY_CHANNEL_DESCRIPTION
        notificationManager.createNotificationChannel(notificationChannel)
    }
}