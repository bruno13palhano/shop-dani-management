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
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.StockOrdersSalesViewModel

@Composable
fun StockOrdersSalesScreen(
    navigateUp: () -> Unit,
    viewModel: StockOrdersSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setStockOrdersSalesRange(7)
    }
    val stockVsOrdersEntries by viewModel.allSales.collectAsStateWithLifecycle()
    val menuOptions = arrayOf(
        stringResource(id = R.string.last_7_days_label),
        stringResource(id = R.string.last_21_days_label),
        stringResource(id = R.string.last_31_days_label)
    )
    val sevenDaysTitle = stringResource(id = R.string.last_7_days_label)
    val twentyOneDaysTitle = stringResource(id = R.string.last_21_days_label)
    val thirtyOneDaysTitle = stringResource(id = R.string.last_31_days_label)
    var chartTitle by remember { mutableStateOf(sevenDaysTitle) }

    ComposedChart(
        screenTitle = stringResource(id = R.string.stock_vs_orders_label),
        startAxisTitle = stringResource(id = R.string.amount_of_sales_label),
        bottomAxisTitle = chartTitle,
        firstChartEntityColor = MaterialTheme.colorScheme.secondary,
        secondChartEntityColor = MaterialTheme.colorScheme.tertiary,
        entry = stockVsOrdersEntries,
        legends = listOf(
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
            when (index) {
                0 -> {
                    viewModel.setStockOrdersSalesRange(7)
                    chartTitle = sevenDaysTitle
                }
                1 -> {
                    viewModel.setStockOrdersSalesRange(21)
                    chartTitle = twentyOneDaysTitle
                }
                2 -> {
                    viewModel.setStockOrdersSalesRange(31)
                    chartTitle = thirtyOneDaysTitle
                }
            }
        },
        navigateUp = navigateUp
    )
}