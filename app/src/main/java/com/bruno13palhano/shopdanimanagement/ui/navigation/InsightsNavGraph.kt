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
import kotlinx.serialization.Serializable

fun NavGraphBuilder.insightsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation<MainRoutes.Insights>(startDestination = InsightsRoutes.Main) {
        composable<InsightsRoutes.Main> {
            InsightsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { route ->
                    navController.navigate(route = route)
                },
                onIconMenuClick = onIconMenuClick,
                goHome = {
                    navController.navigate(route = HomeRoutes.Main) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<InsightsRoutes.Charts> {
            ChartsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<InsightsRoutes.LastSales> {
            LastSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<InsightsRoutes.StockOrdersSales> {
            StockOrdersSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<InsightsRoutes.CompanySales> {
            SalesByCompanyRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface InsightsRoutes {
    @Serializable
    object Main

    @Serializable
    object Charts

    @Serializable
    object LastSales

    @Serializable
    object StockOrdersSales

    @Serializable
    object CompanySales
}