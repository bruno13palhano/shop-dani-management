package com.bruno13palhano.shopdanimanagement.ui.screens.insights

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
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.LastSalesViewModel

@Composable
fun LastSalesScreen(
    navigateUp: () -> Unit,
    viewModel: LastSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setLastSalesEntryByRange(7)
    }
    val lastSalesEntry by viewModel.lastSalesEntry.collectAsStateWithLifecycle()
    val menuOptions = arrayOf(
        stringResource(id = R.string.last_7_days_label),
        stringResource(id = R.string.last_21_days_label),
        stringResource(id = R.string.last_31_days_label)
    )
    val sevenDaysTitle = stringResource(id = R.string.last_7_days_label)
    val twentyOneDaysTitle = stringResource(id = R.string.last_21_days_label)
    val thirtyOneDaysTitle = stringResource(id = R.string.last_31_days_label)
    var chartTitle by remember { mutableStateOf(sevenDaysTitle) }

    SimpleChart(
        screenTitle = stringResource(id = R.string.last_sales_label),
        startAxisTitle = stringResource(id = R.string.amount_of_sales_label),
        bottomAxisTitle = chartTitle,
        entityColors = listOf(
            MaterialTheme.colorScheme.primary
        ),
        entry = lastSalesEntry,
        legends = listOf(),
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.setLastSalesEntryByRange(7)
                    chartTitle = sevenDaysTitle
                }
                1 -> {
                    viewModel.setLastSalesEntryByRange(21)
                    chartTitle = twentyOneDaysTitle
                }
                2 -> {
                    viewModel.setLastSalesEntryByRange(31)
                    chartTitle = thirtyOneDaysTitle
                }
                else -> {}
            }
        },
        navigateUp = navigateUp
    )
}