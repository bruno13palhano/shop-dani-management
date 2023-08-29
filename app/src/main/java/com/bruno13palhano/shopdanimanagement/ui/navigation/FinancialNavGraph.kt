package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialScreen

fun NavGraphBuilder.financialNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = FinancialDestinations.FINANCIAL_MAIN_ROUTE,
        route = MainDestinations.FINANCIAL_ROUTE
    ) {
        composable(route = FinancialDestinations.FINANCIAL_MAIN_ROUTE) {
            showBottomMenu(false)
            FinancialScreen(
                onIconMenuClick = onIconMenuClick
            )
        }
    }
}

object FinancialDestinations {
    const val FINANCIAL_MAIN_ROUTE = "financial_main_route"
}