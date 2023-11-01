package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveryScreen

fun NavGraphBuilder.deliveriesNAvGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE,
        route = HomeDestinations.HOME_DELIVERIES_ROUTE
    ) {
        composable(route = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE) {
            showBottomMenu(true)
            DeliveriesScreen(
                onItemClick = { deliveryId ->
                    navController.navigate(
                        route = "${DeliveriesDestinations.DELIVERIES_DELIVERY_ROUTE}/$deliveryId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${DeliveriesDestinations.DELIVERIES_DELIVERY_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { deliveryId ->
                DeliveryScreen(
                    deliveryId = deliveryId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object DeliveriesDestinations {
    const val DELIVERIES_MAIN_ROUTE = "deliveries_main_route"
    const val DELIVERIES_DELIVERY_ROUTE = "deliveries_delivery_route"
}