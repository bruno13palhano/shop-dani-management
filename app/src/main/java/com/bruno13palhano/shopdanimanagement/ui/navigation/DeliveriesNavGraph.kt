package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.deliveriesNAvGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE,
        route = HomeDestinations.HOME_DELIVERIES_ROUTE
    ) {
        showBottomMenu(true)
        composable(route = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE) {
            DeliveriesScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object DeliveriesDestinations {
    const val DELIVERIES_MAIN_ROUTE = "deliveries_main_route"
}