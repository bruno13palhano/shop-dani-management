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
    isLogged: Boolean,
    showBottomMenu: (show: Boolean) -> Unit = {},
    gesturesEnabled: (enabled: Boolean) -> Unit = {},
    onIconMenuClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLogged) startDestination else MainDestinations.LOGIN_ROUTE,
        modifier = modifier
    ) {
        homeNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
            gesturesEnabled = gesturesEnabled
        )
        financialNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
            gesturesEnabled = gesturesEnabled
        )
        insightsNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
            gesturesEnabled = gesturesEnabled
        )
        productsNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
            gesturesEnabled = gesturesEnabled
        )
        customersNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            onIconMenuClick = onIconMenuClick,
            gesturesEnabled = gesturesEnabled
        )
        loginNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val LOGIN_ROUTE = "login_route"
    const val INSIGHTS_ROUTE = "insights_route"
    const val FINANCIAL_ROUTE = "financial_route"
    const val PRODUCTS_ROUTE = "products_route"
    const val CUSTOMERS_ROUTE = "customers_route"
}
