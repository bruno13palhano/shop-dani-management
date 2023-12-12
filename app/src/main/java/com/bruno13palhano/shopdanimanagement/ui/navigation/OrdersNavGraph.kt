package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesScreen

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
            SalesScreen(
                screenTitle = stringResource(id = R.string.orders_list_label),
                onItemClick = {
                    navController.navigate(
                        route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/$it"
                    )
                },
                onAddButtonClick = { },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { id ->
                SaleScreen(
                    isEdit = true,
                    screenTitle = stringResource(id = R.string.edit_sale_label),
                    isOrderedByCustomer = true,
                    stockOrderId = 0L,
                    saleId = id,
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