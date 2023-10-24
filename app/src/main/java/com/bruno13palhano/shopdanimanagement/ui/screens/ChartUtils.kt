package com.bruno13palhano.shopdanimanagement.ui.screens

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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