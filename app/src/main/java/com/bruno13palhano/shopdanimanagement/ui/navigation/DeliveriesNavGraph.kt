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
        composable(route = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE) {
            showBottomMenu(true)
            DeliveriesScreen(
                onItemClick = { deliveryId ->
                    navController.navigate(route = "$DeliveriesDestinations.DELIVERIES_EDIT_DELIVERY_ROUTE$deliveryId")
                },
                onAddButtonClick = {
                    navController.navigate(route = DeliveriesDestinations.DELIVERIES_NEW_DELIVERY_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = DeliveriesDestinations.DELIVERIES_NEW_DELIVERY_ROUTE) {
            showBottomMenu(true)
        }
        composable(route = DeliveriesDestinations.DELIVERIES_EDIT_DELIVERY_WITH_ID_ROUTE) {
            showBottomMenu(true)

        }
    }
}

object DeliveriesDestinations {
    const val DELIVERIES_MAIN_ROUTE = "deliveries_main_route"
    const val DELIVERIES_NEW_DELIVERY_ROUTE = "deliveries_new_delivery_route"
    const val DELIVERIES_EDIT_DELIVERY_ROUTE = "deliveries_edit_delivery_route"
    const val DELIVERIES_EDIT_DELIVERY_WITH_ID_ROUTE = "$DELIVERIES_EDIT_DELIVERY_ROUTE{$ITEM_ID}"
}