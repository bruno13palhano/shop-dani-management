package com.bruno13palhano.shopdanimanagement.ui.screens

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.Uri
import com.bruno13palhano.shopdanimanagement.ui.notifications.ExpiredProductsNotification
import com.bruno13palhano.shopdanimanagement.ui.notifications.receivers.ExpiredProductsReceiver
import okio.IOException
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.Throws

private const val NOTIFICATION_ACTION_PREFIX = "com.bruno13palhano.shopdanimanagement"

val currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

val dateFormat: DateFormat =
    SimpleDateFormat.getDateInstance().apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

fun setAlarmNotification(
    id: Long,
    title: String,
    date: Long,
    description: String,
    context: Context
) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notifyIntent = Intent(context, ExpiredProductsReceiver::class.java)
    notifyIntent.apply {
        action = "$NOTIFICATION_ACTION_PREFIX.$id"
        putExtra("id", id.toInt())
        putExtra("title", title)
        putExtra("description", description)
    }

    val notifyPendingIntent =
        PendingIntent.getBroadcast(
            context,
            id.toInt(),
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    val alarmNotification =
        ExpiredProductsNotification(
            notificationManager = notificationManager,
            alarmManager = alarmManager
        )

    alarmNotification.setAlarmManager(notifyPendingIntent = notifyPendingIntent, date = date)
}

@Throws(IOException::class)
fun getBytes(
    context: Context,
    uri: Uri
): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use {
        val outputStream = ByteArrayOutputStream()
        val byteArray = it.buffered().readBytes()
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val isCompressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)

        return if (isCompressed) outputStream.toByteArray() else byteArray
    }
}