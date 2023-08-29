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
    onIconMenuClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick
        )
        financialNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick
        )
        insightsNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick
        )
        productsNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
        )
        customersNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
        )
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val INSIGHTS_ROUTE = "insights_route"
    const val FINANCIAL_ROUTE = "financial_route"
    const val PRODUCTS_ROUTE = "products_route"
    const val CUSTOMERS_ROUTE = "customers_route"
}
