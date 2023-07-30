package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.shopdanimanagement.ui.screens.HomeScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.RequestsScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.SalesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.ShoppingScreen

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
        val bottomNavActions = BottomMenuNavActions(navController)
        composable(route = MainDestinations.HOME_ROUTE) {
            HomeScreen(
                destinationsHierarchy = it.destination.hierarchy,
                onBottomMenuItemClick = { route ->
                    bottomNavActions.navigateFromBottomMenu(route)
                },
                onMenuClick = onMenuClick
            )
        }
        stockNavGraph(
            navController = navController,
            onMenuClick = onMenuClick
        )
        composable(route = MainDestinations.SHOPPING_ROUTE) {
            ShoppingScreen(
                destinationsHierarchy = it.destination.hierarchy,
                onBottomMenuItemClick = { route ->
                    bottomNavActions.navigateFromBottomMenu(route)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = MainDestinations.SALES_ROUTE) {
            SalesScreen(
                destinationsHierarchy = it.destination.hierarchy,
                onBottomMenuItemClick = { route ->
                    bottomNavActions.navigateFromBottomMenu(route)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = MainDestinations.REQUESTS_ROUTE) {
            RequestsScreen(
                destinationsHierarchy = it.destination.hierarchy,
                onBottomMenuItemClick = bottomNavActions.navigateFromBottomMenu,
                onMenuClick = onMenuClick
            )
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
