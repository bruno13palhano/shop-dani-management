package com.bruno13palhano.shopdanimanagement.ui.theme.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.stock.StockListScreen

fun NavGraphBuilder.stockNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = StockDestinations.MAIN_STOCK_ROUTE,
        route = MainDestinations.STOCK_ROUTE
    ) {
        composable(route = StockDestinations.MAIN_STOCK_ROUTE) {
            StockScreen(
                onClick = { route ->
                    navController.navigate(route)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = StockDestinations.STOCK_LIST_ROUTE) {
            StockListScreen(
                navigateBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}

object StockDestinations {
    const val MAIN_STOCK_ROUTE = "main_stock_route"
    const val STOCK_LIST_ROUTE = "stock_list_route"
}