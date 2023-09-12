package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleChart
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.CompanySalesViewModel

@Composable
fun CompanySalesScreen(
    navigateUp: () -> Unit,
    viewModel: CompanySalesViewModel = hiltViewModel()
) {
    val chartEntry by viewModel.chartEntry.collectAsStateWithLifecycle()

    SimpleChart(
        screenTitle = ,
        startAxisTitle = ,
        bottomAxisTitle = ,
        entityColors = ,
        entry = ,
        legends = ,
        menuOptions = ,
        onMenuItemClick = ,
        navigateUp = navigateUp
    )
}