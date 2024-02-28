package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.OrdersRoute

fun NavGraphBuilder.ordersNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.ORDERS_MAIN_ROUTE,
        route = HomeDestinations.HOME_ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.ORDERS_MAIN_ROUTE) {
            OrdersRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { orderId ->
                    navController.navigate(
                        route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/$orderId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { id ->
                EditSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    saleId = id,
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