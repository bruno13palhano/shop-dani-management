package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.ChartsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.InsightsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.LastSalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.SalesByCompanyRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.StockOrdersSalesRoute

fun NavGraphBuilder.insightsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = InsightsDestinations.INSIGHTS_MAIN_ROUTE,
        route = MainDestinations.INSIGHTS_ROUTE
    ) {
        composable(route = InsightsDestinations.INSIGHTS_MAIN_ROUTE) {
            InsightsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { route ->
                    navController.navigate(route = route)
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
        composable(route = InsightsDestinations.INSIGHTS_CHARTS_ROUTE) {
            ChartsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = InsightsDestinations.INSIGHTS_LAST_SALES_ROUTE) {
            LastSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = InsightsDestinations.INSIGHTS_STOCK_ORDERS_ROUTE) {
            StockOrdersSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = InsightsDestinations.INSIGHTS_COMPANY_SALES_ROUTE) {
            SalesByCompanyRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object InsightsDestinations {
    const val INSIGHTS_MAIN_ROUTE = "insights_main_route"
    const val INSIGHTS_CHARTS_ROUTE = "insights_charts_route"
    const val INSIGHTS_LAST_SALES_ROUTE = "insights_last_sales_route"
    const val INSIGHTS_STOCK_ORDERS_ROUTE = "insights_stock_orders_route"
    const val INSIGHTS_COMPANY_SALES_ROUTE = "insights_company_sales_route"
}