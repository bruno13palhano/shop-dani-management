package com.bruno13palhano.shopdanimanagement.ui.screens


import android.icu.text.DateFormat
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

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


fun stringToFloat(value: String): Float {
    return try {
        value.replace(",", ".").toFloat()
    } catch (ignored: Exception) { 0F }
}

fun stringToInt(value: String): Int {
    return try {
        value.toInt()
    } catch (ignored: Exception) { 0 }
}

val currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

val dateFormat: DateFormat = SimpleDateFormat.getDateInstance().apply {
    timeZone = TimeZone.getTimeZone("UTC")
}