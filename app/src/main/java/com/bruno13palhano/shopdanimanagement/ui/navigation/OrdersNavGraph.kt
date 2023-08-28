package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.OrdersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.EditItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderSearchScreen

private const val ITEM_ID = "item_Id"

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
            OrdersScreen(
                onProductsClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_LIST_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_SEARCH_ITEM_ROUTE) {
            showBottomMenu(true)
            StockOrderSearchScreen(
                isOrderedByCustomer = true,
                onItemClick = { productId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}$productId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_LIST_ROUTE) {
            showBottomMenu(true)
            StockOrderListScreen(
                isOrderedByCustomer = true,
                isAddButtonEnabled = false,
                screenTitle = stringResource(id = R.string.orders_list_label),
                onItemClick = { productId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}$productId")
                },
                onSearchClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_SEARCH_ITEM_ROUTE)
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_EDIT_ITEM_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { orderItemId ->
                EditItemScreen(
                    stockOrderItemId = orderItemId.toLong(),
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
    const val ORDERS_LIST_ROUTE = "orders_list_route"
    const val ORDERS_EDIT_ITEM_ROUTE = "orders_edit_item_route"
    const val ORDERS_EDIT_ITEM_WITH_ID_ROUTE = "$ORDERS_EDIT_ITEM_ROUTE{$ITEM_ID}"
}