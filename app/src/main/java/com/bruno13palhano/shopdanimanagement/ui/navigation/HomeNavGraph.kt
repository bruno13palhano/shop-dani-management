package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = HomeDestinations.HOME_MAIN_ROUTE,
        route = MainDestinations.HOME_ROUTE,
    ) {
        composable(route = HomeDestinations.HOME_MAIN_ROUTE) {
            showBottomMenu(true)
            HomeScreen(
                onOptionsItemClick = { route ->
                    navController.navigate(route = route)
                },
                onMenuClick = onIconMenuClick,
            )
        }
        salesNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
        stockNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
        ordersNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
        deliveriesNAvGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
        catalogNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
    }
}

object HomeDestinations {
    const val HOME_MAIN_ROUTE = "home_main_route"
    const val HOME_STOCK_ROUTE = "home_stock_route"
    const val HOME_SALES_ROUTE = "home_sales_route"
    const val HOME_ORDERS_ROUTE = "home_orders_route"
    const val HOME_DELIVERIES_ROUTE = "home_deliveries_route"
    const val HOME_CATALOG_ROUTE = "home_catalog_route"
}