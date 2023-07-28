package com.bruno13palhano.shopdanimanagement.ui.screens


import android.icu.text.DecimalFormat
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