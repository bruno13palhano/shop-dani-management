package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.HOME_ROUTE,
    showBottomMenu: (show: Boolean) -> Unit = {},
    onMenuClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeNavGraph(
            navController = navController,
            onMenuClick = onMenuClick,
            showBottomMenu = showBottomMenu
        )
        productsNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
        customersNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val PRODUCTS_ROUTE = "products_route"
    const val CUSTOMERS_ROUTE = "customers_route"
}
