package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.OrdersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.EditItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderListScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.ordersNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.MAIN_ORDERS_ROUTE,
        route = MainDestinations.ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.MAIN_ORDERS_ROUTE) {
            OrdersScreen(
                onSearchClick = {
                    navController.navigate(OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE)
                },
                onMenuClick = onMenuClick,
                onProductsClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_LIST_ROUTE)
                }
            )
        }
        composable(route = OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE) {
            SearchProductScreen(
                onItemClick = { productId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE}$productId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_LIST_ROUTE) {
            StockOrderListScreen(
                isOrderedByCustomer = true,
                screenTitle = stringResource(id = R.string.orders_list_label),
                onItemClick = { productId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_EDIT_PRODUCT_ROUTE}$productId")
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
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
    const val MAIN_ORDERS_ROUTE = "main_orders_route"
    const val ORDERS_SEARCH_PRODUCT_ROUTE = "orders_search_product_route"
    const val ORDERS_LIST_ROUTE = "orders_list_route"
    const val ORDERS_EDIT_PRODUCT_ROUTE = "orders_edit_product_route"
    const val ORDERS_EDIT_PRODUCT_WITH_ID_ROUTE = "$ORDERS_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}