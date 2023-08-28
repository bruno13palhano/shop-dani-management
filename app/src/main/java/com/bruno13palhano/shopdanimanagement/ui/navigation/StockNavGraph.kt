package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.ItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrderSearchScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrdersScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.stockNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = StockDestinations.STOCK_MAIN_ROUTE,
        route = HomeDestinations.HOME_STOCK_ROUTE
    ) {
        composable(route = StockDestinations.STOCK_MAIN_ROUTE) {
            showBottomMenu(true)
            StockOrdersScreen(
                isOrderedByCustomer = false,
                isAddButtonEnabled = true,
                screenTitle = stringResource(id = R.string.stock_list_label),
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_ITEM_ROUTE}$productId"
                    )
                },
                onSearchClick = {
                    navController.navigate(route = StockDestinations.STOCK_SEARCH_ITEM_ROUTE)
                },
                onAddButtonClick = {
                    navController.navigate(route = StockDestinations.STOCK_ITEM_LIST_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_ITEM_ROUTE) {
            showBottomMenu(true)
            StockOrderSearchScreen(
                isOrderedByCustomer = false,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_ITEM_ROUTE}$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_ITEM_LIST_ROUTE) {
            showBottomMenu(true)
            ProductListScreen(
                isEditable = false,
                categoryId = 0L,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_NEW_ITEM_ROUTE}$productId"
                    )
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_NEW_ITEM_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                ItemScreen(
                    isEditable = false,
                    stockOrderItemId = 0L,
                    isOrderedByCustomer = false,
                    screenTitle = stringResource(id = R.string.new_stock_item_label),
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = StockDestinations.STOCK_EDIT_ITEM_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { stockItemId ->
                ItemScreen(
                    isEditable = true,
                    productId = 0L,
                    stockOrderItemId = stockItemId.toLong(),
                    isOrderedByCustomer = false,
                    screenTitle = stringResource(id = R.string.edit_stock_item_label),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object StockDestinations {
    const val STOCK_MAIN_ROUTE = "stock_main_route"
    const val STOCK_SEARCH_ITEM_ROUTE = "stock_search_item_route"
    const val STOCK_ITEM_LIST_ROUTE = "stock_item_list_route"
    const val STOCK_NEW_ITEM_ROUTE = "stock_new_item_route"
    const val STOCK_NEW_ITEM_WITH_ID_ROUTE = "$STOCK_NEW_ITEM_ROUTE{$ITEM_ID}"
    const val STOCK_EDIT_ITEM_ROUTE = "stock_edit_item_route"
    const val STOCK_EDIT_ITEM_WITH_ID_ROUTE = "$STOCK_EDIT_ITEM_ROUTE{$ITEM_ID}"
}