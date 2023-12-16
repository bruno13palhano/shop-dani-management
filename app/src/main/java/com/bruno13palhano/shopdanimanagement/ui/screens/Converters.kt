package com.bruno13palhano.shopdanimanagement.ui.screens

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