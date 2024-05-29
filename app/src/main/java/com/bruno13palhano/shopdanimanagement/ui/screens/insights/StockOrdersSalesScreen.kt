package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import android.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ComposedChart
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.StockOrdersSalesViewModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus

@Composable
fun StockOrdersSalesRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    StockOrdersSalesScreen(navigateUp = navigateUp)
}

@Composable
fun StockOrdersSalesScreen(
    navigateUp: () -> Unit,
    viewModel: StockOrdersSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setStockOrdersSalesRange(7)
    }
    val stockVsOrdersEntries by viewModel.allSales.collectAsStateWithLifecycle()
    val menuOptions =
        arrayOf(
            stringResource(id = R.string.last_7_days_label),
            stringResource(id = R.string.last_21_days_label),
            stringResource(id = R.string.last_31_days_label)
        )

    var chartTitle by remember { mutableStateOf(menuOptions[0]) }
    val stockChart by remember { mutableStateOf(ChartEntryModelProducer()) }
    val ordersChart by remember { mutableStateOf(ChartEntryModelProducer()) }
    val chart by remember { mutableStateOf(stockChart + ordersChart) }

    LaunchedEffect(key1 = stockVsOrdersEntries) {
        stockChart.setEntries(
            stockVsOrdersEntries.stockEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
        ordersChart.setEntries(
            stockVsOrdersEntries.ordersEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    ComposedChart(
        screenTitle = stringResource(id = R.string.stock_vs_orders_label),
        startAxisTitle = stringResource(id = R.string.amount_of_sales_label),
        bottomAxisTitle = chartTitle,
        firstChartEntityColor = MaterialTheme.colorScheme.primary,
        secondChartEntityColor = MaterialTheme.colorScheme.tertiary,
        entry = chart,
        legends =
            listOf(
                Pair(
                    stringResource(id = R.string.stock_label),
                    Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.orders_label),
                    Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
                )
            ),
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle =
                when (index) {
                    StockOrdersSalesMenu.DAYS_7 -> {
                        viewModel.setStockOrdersSalesRange(7)
                        menuOptions[0]
                    }
                    StockOrdersSalesMenu.DAYS_21 -> {
                        viewModel.setStockOrdersSalesRange(21)
                        menuOptions[1]
                    }
                    StockOrdersSalesMenu.DAYS_31 -> {
                        viewModel.setStockOrdersSalesRange(31)
                        menuOptions[2]
                    }
                    else -> {
                        viewModel.setStockOrdersSalesRange(7)
                        menuOptions[0]
                    }
                }
        },
        navigateUp = navigateUp
    )
}

private object StockOrdersSalesMenu {
    const val DAYS_7 = 0
    const val DAYS_21 = 1
    const val DAYS_31 = 2
}