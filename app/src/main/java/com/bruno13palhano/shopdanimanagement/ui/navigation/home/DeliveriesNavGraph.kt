package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveryRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.deliveriesNAvGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Deliveries>(startDestination = DeliveriesRoutes.Main) {
        composable<DeliveriesRoutes.Main> {
            DeliveriesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { deliveryId ->
                    navController.navigate(route = DeliveriesRoutes.Delivery(id = deliveryId))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<DeliveriesRoutes.Delivery> { backStackEntry ->
            val deliveryId = backStackEntry.toRoute<DeliveriesRoutes.Delivery>().id

            DeliveryRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                deliveryId = deliveryId,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed class DeliveriesRoutes {
    @Serializable
    object Main

    @Serializable
    data class Delivery(val id: Long)
}