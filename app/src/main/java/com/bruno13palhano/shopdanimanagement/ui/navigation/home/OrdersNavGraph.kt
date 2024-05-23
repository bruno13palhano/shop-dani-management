package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.PRODUCT_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.SALE_ID
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.OrdersRoute

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.ordersNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.ORDERS_MAIN_ROUTE,
        route = HomeDestinations.HOME_ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.ORDERS_MAIN_ROUTE) {
            OrdersRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { orderId, productId ->
                    navController.navigate(
                        route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/$orderId/$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/{$SALE_ID}/{$PRODUCT_ID}",
            arguments = listOf(
                navArgument(SALE_ID) { type = NavType.LongType },
                navArgument(PRODUCT_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getLong(SALE_ID)
            val productId = backStackEntry.arguments?.getLong(PRODUCT_ID)

            if (saleId != null && productId != null) {
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
}

object OrdersDestinations {
    const val ORDERS_MAIN_ROUTE = "orders_main_route"
    const val ORDERS_EDIT_ITEM_ROUTE = "orders_edit_item_route"
}