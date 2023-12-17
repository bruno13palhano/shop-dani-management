package com.bruno13palhano.shopdanimanagement.ui.screens

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

fun stringToLong(value: String) = try { value.toLong() } catch (ignored: Exception) { 0L }

fun getCurrentTimestamp(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    .format(OffsetDateTime.now(ZoneOffset.UTC))