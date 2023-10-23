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
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleChart
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.SalesByCompanyViewModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun SalesByCompanyScreen(
    navigateUp: () -> Unit,
    viewModel: SalesByCompanyViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getChartByRange(7)
    }
    val salesByCompanyEntries by viewModel.chartEntry.collectAsStateWithLifecycle()
    val menuOptions = arrayOf(
        stringResource(id = R.string.last_7_days_label),
        stringResource(id = R.string.last_21_days_label),
        stringResource(id = R.string.last_31_days_label)
    )

    var chartTitle by remember { mutableStateOf(menuOptions[0]) }
    val chart by remember { mutableStateOf(ChartEntryModelProducer()) }

    LaunchedEffect(key1 = salesByCompanyEntries) {
        chart.setEntries(
            salesByCompanyEntries.avonEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            },
            salesByCompanyEntries.naturaEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    SimpleChart(
        screenTitle = stringResource(id = R.string.company_sales_label),
        startAxisTitle = stringResource(id = R.string.amount_of_money_label),
        bottomAxisTitle = chartTitle,
        entityColors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        ),
        entry = chart,
        legends = listOf(
            Pair(
                stringResource(id = R.string.avon_company_label),
                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
            ),
            Pair(
                stringResource(id = R.string.natura_company_label),
                Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
            )
        ),
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle = when (index) {
                1 -> {
                    viewModel.getChartByRange(21)
                    menuOptions[1]
                }
                2 -> {
                    viewModel.getChartByRange(31)
                    menuOptions[2]
                }
                else -> {
                    viewModel.getChartByRange(7)
                    menuOptions[0]
                }
            }
        },
        navigateUp = navigateUp
    )
}