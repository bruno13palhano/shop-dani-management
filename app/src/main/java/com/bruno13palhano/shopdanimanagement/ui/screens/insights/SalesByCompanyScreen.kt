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
fun SalesByCompanyRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    SalesByCompanyScreen(navigateUp = navigateUp)
}

@Composable
fun SalesByCompanyScreen(
    navigateUp: () -> Unit,
    viewModel: SalesByCompanyViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getChartByRange(7)
    }
    val salesByCompanyEntries by viewModel.chartEntry.collectAsStateWithLifecycle()
    val menuOptions =
        arrayOf(
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
            },
            salesByCompanyEntries.boticarioEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            },
            salesByCompanyEntries.eudoraEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            },
            salesByCompanyEntries.bereniceEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            },
            salesByCompanyEntries.ouiEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    SimpleChart(
        screenTitle = stringResource(id = R.string.company_sales_label),
        startAxisTitle = stringResource(id = R.string.amount_of_sales_label),
        bottomAxisTitle = chartTitle,
        entityColors =
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.error,
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.inversePrimary
            ),
        entry = chart,
        legends =
            listOf(
                Pair(
                    stringResource(id = R.string.avon_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.natura_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.boticario_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.secondary.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.eudora_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.error.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.berenice_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.errorContainer.value.toLong())
                ),
                Pair(
                    stringResource(id = R.string.oui_company_label),
                    Color.toArgb(MaterialTheme.colorScheme.inversePrimary.value.toLong())
                )
            ),
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle =
                when (index) {
                    SalesByCompanyMenu.DAYS_7 -> {
                        viewModel.getChartByRange(7)
                        menuOptions[0]
                    }
                    SalesByCompanyMenu.DAYS_21 -> {
                        viewModel.getChartByRange(21)
                        menuOptions[1]
                    }
                    SalesByCompanyMenu.DAYS_31 -> {
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

private object SalesByCompanyMenu {
    const val DAYS_7 = 0
    const val DAYS_21 = 1
    const val DAYS_31 = 2
}