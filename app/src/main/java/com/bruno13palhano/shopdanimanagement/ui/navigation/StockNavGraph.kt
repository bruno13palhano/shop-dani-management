package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.NewItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ProductItemListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.EditStockItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderListScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.stockNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = StockDestinations.MAIN_STOCK_ROUTE,
        route = MainDestinations.STOCK_ROUTE
    ) {
        composable(route = StockDestinations.MAIN_STOCK_ROUTE) {
            StockScreen(
                onProductsClick = {
                    navController.navigate(StockDestinations.STOCK_LIST_ROUTE)
                },
                onSearchClick = {
                    navController.navigate(StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE) {
            SearchProductScreen(
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_PRODUCT_ROUTE}$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_LIST_ROUTE) {
            StockOrderListScreen(
                isOrderedByCustomer = false,
                screenTitle = stringResource(id = R.string.stock_list_label),
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_PRODUCT_ROUTE}$productId"
                    )
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = StockDestinations.STOCK_PRODUCTS_STOCK_LIST_ROUTE
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_PRODUCTS_STOCK_LIST_ROUTE) {
            ProductItemListScreen(
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_NEW_PRODUCT_ROUTE}$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_NEW_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                NewItemScreen(
                    isOrderedByCustomer = false,
                    screenTitle = stringResource(id = R.string.new_stock_item_label),
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = StockDestinations.STOCK_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { stockItemId ->
                EditStockItemScreen(
                    stockItemId = stockItemId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object StockDestinations {
    const val MAIN_STOCK_ROUTE = "main_stock_route"
    const val STOCK_SEARCH_PRODUCT_ROUTE = "stock_search_product_route"
    const val STOCK_LIST_ROUTE = "stock_list_route"
    const val STOCK_PRODUCTS_STOCK_LIST_ROUTE = "stock_products_stock_list_route"
    const val STOCK_NEW_PRODUCT_ROUTE = "stock_new_product_route"
    const val STOCK_NEW_PRODUCT_WITH_ID_ROUTE = "$STOCK_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val STOCK_EDIT_PRODUCT_ROUTE = "stock_edit_product_route"
    const val STOCK_EDIT_PRODUCT_WITH_ID_ROUTE = "$STOCK_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}