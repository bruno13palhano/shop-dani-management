package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
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
                onIconMenuClick = onIconMenuClick
            )
        }
    }
}

object InsightsDestinations {
    const val INSIGHTS_MAIN_ROUTE = "insights_main_route"
}