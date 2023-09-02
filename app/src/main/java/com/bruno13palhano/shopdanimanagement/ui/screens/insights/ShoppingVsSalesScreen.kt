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
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.ShoppingVsSalesViewModel

@Composable
fun ShoppingVsSalesScreen(
    navigateUp: () -> Unit,
    viewModel: ShoppingVsSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getChartByRange(7)
    }
    val chartEntry by viewModel.chartEntry.collectAsStateWithLifecycle()
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
        screenTitle = stringResource(id = R.string.shopping_vs_sales_label),
        startAxisTitle = stringResource(id = R.string.amount_of_items_sales_label),
        bottomAxisTitle = chartTitle,
        entityColors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        ),
        entry = chartEntry,
        legends = listOf(
            Pair(
                stringResource(id = R.string.sales_label),
                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
            ),
            Pair(
                stringResource(id = R.string.shopping_label),
                Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
            )
        ),
        menuOptions = menuOptions,
        onMenuItemClick = {index ->
            chartTitle = when (index) {
                1 -> {
                    viewModel.getChartByRange(21)
                    twentyOneDaysTitle
                }
                2 -> {
                    viewModel.getChartByRange(31)
                    thirtyOneDaysTitle
                }
                else -> {
                    viewModel.getChartByRange(7)
                    sevenDaysTitle
                }
            }
        },
        navigateUp = navigateUp
    )
}