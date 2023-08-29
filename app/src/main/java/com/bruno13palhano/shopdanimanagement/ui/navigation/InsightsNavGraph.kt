package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.InsightsScreen

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
    }
}

object InsightsDestinations {
    const val INSIGHTS_MAIN_ROUTE = "insights_main_route"
}