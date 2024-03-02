package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveryRoute

fun NavGraphBuilder.deliveriesNAvGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE,
        route = HomeDestinations.HOME_DELIVERIES_ROUTE
    ) {
        composable(route = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE) {
            DeliveriesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
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
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { deliveryId ->
                DeliveryRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
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