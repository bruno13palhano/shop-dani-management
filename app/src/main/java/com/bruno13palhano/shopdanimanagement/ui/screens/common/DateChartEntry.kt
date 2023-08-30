package com.bruno13palhano.shopdanimanagement.ui.screens.common

import com.patrykandpatrick.vico.core.entry.ChartEntry

class DateChartEntry(
    val date: String,
    override val x: Float,
    override val y: Float
) : ChartEntry {
    override fun withY(y: Float): ChartEntry = DateChartEntry(date, x, y)
}