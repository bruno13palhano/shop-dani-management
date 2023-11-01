package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.ItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrderSearchScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrdersScreen

fun NavGraphBuilder.ordersNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.ORDERS_MAIN_ROUTE,
        route = HomeDestinations.HOME_ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.ORDERS_MAIN_ROUTE) {
            showBottomMenu(true)
            StockOrdersScreen(
                isOrderedByCustomer = true,
                isAddButtonEnabled = false,
                screenTitle = stringResource(id = R.string.orders_list_label),
                onItemClick = { orderId ->
                    navController.navigate(
                        route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/$orderId"
                    )
                },
                onSearchClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_SEARCH_ITEM_ROUTE)
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_SEARCH_ITEM_ROUTE) {
            showBottomMenu(true)
            StockOrderSearchScreen(
                isOrderedByCustomer = true,
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
            showBottomMenu(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { orderItemId ->
                ItemScreen(
                    isEditable = true,
                    productId = 0L,
                    stockOrderItemId = orderItemId,
                    isOrderedByCustomer = true,
                    screenTitle = stringResource(id = R.string.edit_order_item_label),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object OrdersDestinations {
    const val ORDERS_MAIN_ROUTE = "orders_main_route"
    const val ORDERS_SEARCH_ITEM_ROUTE = "orders_search_item_route"
    const val ORDERS_EDIT_ITEM_ROUTE = "orders_edit_item_route"
}