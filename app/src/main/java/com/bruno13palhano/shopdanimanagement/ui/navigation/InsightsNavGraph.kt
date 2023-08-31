package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.ChartsScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.InsightsScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.LastSalesScreen

fun NavGraphBuilder.insightsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = InsightsDestinations.INSIGHTS_MAIN_ROUTE,
        route = MainDestinations.INSIGHTS_ROUTE
    ) {
        composable(route = InsightsDestinations.INSIGHTS_MAIN_ROUTE) {
            showBottomMenu(false)
            InsightsScreen(
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
            ChartsScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = InsightsDestinations.INSIGHTS_LAST_SALES_ROUTE) {
            LastSalesScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object InsightsDestinations {
    const val INSIGHTS_MAIN_ROUTE = "insights_main_route"
    const val INSIGHTS_CHARTS_ROUTE = "insights_charts_route"
    const val INSIGHTS_LAST_SALES_ROUTE = "insights_last_sales_route"
}