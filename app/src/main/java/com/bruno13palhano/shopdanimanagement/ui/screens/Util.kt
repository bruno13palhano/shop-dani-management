package com.bruno13palhano.shopdanimanagement.ui.screens


import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.Uri
import com.bruno13palhano.shopdanimanagement.ui.notifications.ExpiredProductsNotification
import com.bruno13palhano.shopdanimanagement.ui.notifications.receivers.ExpiredProductsReceiver
import okio.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.jvm.Throws

private const val NOTIFICATION_ACTION_PREFIX = "com.bruno13palhano.shopdanimanagement"

/**
 * Formats the string with local decimal separator.
 * @param value the string to be formatted.
 * @return the new string formatted.
 */
fun formatWithLocalDecimal(value: String): String {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    return if (decimalSeparator == ',') {
        value.replace(".", ",")
    } else {
        value.replace(",", ".")
    }
}

fun setQuantity(days: Array<Int>, date: Long, quantity: Int) {
    val currentDay = LocalDate.now()

    for (i in days.indices) {
        if (LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("UTC")).toLocalDate()
            == currentDay.minusDays(i.toLong())) {
            days[i] += quantity
        }
    }
}

fun setChartEntries(chart: MutableList<Pair<String, Float>>, days: Array<Int>) {
    val currentDay = LocalDate.now()

    for (i in days.size-1 downTo 0) {
        chart.add(
            Pair(
                DateTimeFormatter.ofPattern("dd/MM").format(currentDay.minusDays(i.toLong())),
                days[i].toFloat()
            )
        )
    }
}

val currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

val dateFormat: DateFormat = SimpleDateFormat.getDateInstance().apply {
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

    val notifyPendingIntent = PendingIntent.getBroadcast(
        context,
        id.toInt(),
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmNotification = ExpiredProductsNotification(
        notificationManager = notificationManager,
        alarmManager = alarmManager
    )

    alarmNotification.setAlarmManager(notifyPendingIntent = notifyPendingIntent, date = date)
}

@Throws(IOException::class)
fun getBytes(context: Context, uri: Uri): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use {
        return it.buffered().readBytes()
    }
}