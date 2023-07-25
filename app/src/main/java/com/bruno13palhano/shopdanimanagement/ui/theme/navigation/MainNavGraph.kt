package com.bruno13palhano.shopdanimanagement.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.HomeScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.RequestsScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.SalesScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.ShoppingScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.StockScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = MainDestinations.HOME_ROUTE) {
            HomeScreen()
        }
        composable(route = MainDestinations.STOCK_ROUTE) {
            StockScreen()
        }
        composable(route = MainDestinations.SHOPPING_ROUTE) {
            ShoppingScreen()
        }
        composable(route = MainDestinations.SALES_ROUTE) {
            SalesScreen()
        }
        composable(route = MainDestinations.REQUESTS_ROUTE) {
            RequestsScreen()
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val STOCK_ROUTE = "stock_route"
    const val SHOPPING_ROUTE = "shopping_route"
    const val SALES_ROUTE = "sales_route"
    const val REQUESTS_ROUTE = "requests_route"
}
