package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.ShoppingItemsScreen

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
                onItemClick = { route ->
                    navController.navigate(route)
                },
                onIconMenuClick = onIconMenuClick,
                goHome = {
                    navController.navigate(route = HomeDestinations.HOME_MAIN_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_SHOPPING_ITEMS) {
            showBottomMenu(false)
            ShoppingItemsScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object FinancialDestinations {
    const val FINANCIAL_MAIN_ROUTE = "financial_main_route"
    const val FINANCIAL_SHOPPING_ITEMS = "financial_shopping_items_route"
}