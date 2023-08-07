package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.OrdersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.orders.OrdersCategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.orders.OrdersListScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.ordersBavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.MAIN_ORDERS_ROUTE,
        route = MainDestinations.ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.MAIN_ORDERS_ROUTE) {
            OrdersScreen(
                onMenuClick = onMenuClick,
                onCategoriesClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_CATEGORIES_ROUTE)
                }
            )
        }
        composable(route = OrdersDestinations.ORDERS_CATEGORIES_ROUTE) {
            OrdersCategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_LIST_ROUTE}$categoryId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                OrdersListScreen(
                    categoryId = categoryId.toLong(),
                    onItemClick = {},
                    onAddButtonClick = { /*TODO*/ },
                    navigateUp = {}
                )
            }
        }
    }
}

object OrdersDestinations {
    const val MAIN_ORDERS_ROUTE = "main_orders_route"
    const val ORDERS_CATEGORIES_ROUTE = "orders_categories_route"
    const val ORDERS_LIST_ROUTE = "orders_list_route/"
    const val ORDERS_LIST_WITH_ID_ROUTE = "$ORDERS_LIST_ROUTE{$ITEM_ID}"
}