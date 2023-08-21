package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.shopdanimanagement.ui.screens.HomeScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.HOME_ROUTE,
    onMenuClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = MainDestinations.HOME_ROUTE) {
            HomeScreen(
                onMenuClick = onMenuClick
            )
        }
        stockNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        shoppingNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        salesNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        ordersNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        productsNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        customersNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val STOCK_ROUTE = "stock_route"
    const val SHOPPING_ROUTE = "shopping_route"
    const val SALES_ROUTE = "sales_route"
    const val ORDERS_ROUTE = "orders_route"
    const val PRODUCTS_ROUTE = "products_route"
    const val CUSTOMERS_ROUTE = "customers_route"
}
