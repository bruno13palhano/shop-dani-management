package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.OrdersRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.ordersNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Orders>(startDestination = OrdersRoutes.Main) {
        composable<OrdersRoutes.Main> {
            OrdersRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { orderId, productId ->
                    navController.navigate(
                        route = OrdersRoutes.EditItem(saleId = orderId, productId = productId)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable<OrdersRoutes.EditItem> { backStackEntry ->
            val saleId = backStackEntry.toRoute<OrdersRoutes.EditItem>().saleId
            val productId = backStackEntry.toRoute<OrdersRoutes.EditItem>().productId

            EditSaleRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                saleId = saleId,
                productId = productId,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface OrdersRoutes {
    @Serializable
    object Main

    @Serializable
    data class EditItem(val saleId: Long, val productId: Long)
}