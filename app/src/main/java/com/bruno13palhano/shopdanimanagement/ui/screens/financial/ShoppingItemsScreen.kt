package com.bruno13palhano.shopdanimanagement.ui.screens.financial

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
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.ShoppingItemsViewModel

@Composable
fun ShoppingItemsScreen(
    navigateUp: () -> Unit,
    viewModel: ShoppingItemsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setChartRangeOfDays(7)
    }
    val stockItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
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
        screenTitle = stringResource(id = R.string.shopping_items_label),
        startAxisTitle = stringResource(id = R.string.amount_of_items_label),
        bottomAxisTitle = chartTitle,
        firstChartEntityColor = MaterialTheme.colorScheme.secondary,
        secondChartEntityColor = MaterialTheme.colorScheme.tertiary,
        entry = stockItems,
        legends = listOf(
            Pair(
                stringResource(id = R.string.paid_label),
                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
            ),
            Pair(
                stringResource(id = R.string.owing_paid_label),
                Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
            )
        ),
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle = when (index) {
                1 -> {
                    viewModel.setChartRangeOfDays(21)
                    twentyOneDaysTitle
                }

                2 -> {
                    viewModel.setChartRangeOfDays(31)
                    thirtyOneDaysTitle
                }

                else -> {
                    viewModel.setChartRangeOfDays(7)
                    sevenDaysTitle
                }
            }
        },
        navigateUp = navigateUp
    )
}