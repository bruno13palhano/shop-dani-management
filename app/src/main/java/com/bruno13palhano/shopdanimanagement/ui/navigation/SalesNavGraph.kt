package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.SalesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.StockOrderSearchScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.salesNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = SalesDestinations.MAIN_SALES_ROUTE,
        route = MainDestinations.SALES_ROUTE
    ) {
        composable(route = SalesDestinations.MAIN_SALES_ROUTE) {
            showBottomMenu(true)
            SalesScreen(
                onItemClick = { saleItemId ->
                    navController.navigate(route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}$saleItemId")
                },
                onMenuClick = onMenuClick,
                onAddButtonClick = {
                    navController.navigate(route = SalesDestinations.SALES_OPTIONS_ROUTE)
                }
            )
        }
        composable(route = SalesDestinations.SALES_OPTIONS_ROUTE) {
            showBottomMenu(true)
            SalesOptionsScreen(
                onOrdersOptionClick = {
                    navController.navigate(route = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE)
                },
                onStockOptionClick = {
                    navController.navigate(route = SalesDestinations.SALES_STOCK_LIST_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_SEARCH_STOCK_ROUTE) {
            showBottomMenu(true)
            StockOrderSearchScreen(
                isOrderedByCustomer = false,
                onItemClick = { stockItem ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}$stockItem"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_STOCK_LIST_ROUTE) {
            showBottomMenu(true)
            StockOrderListScreen(
                isOrderedByCustomer = false,
                isAddButtonEnabled = false,
                screenTitle = stringResource(id = R.string.stock_list_label),
                onItemClick = { stockItemId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_NEW_SALE_STOCK_ROUTE}$stockItemId"
                    )
                },
                onSearchClick = {
                    navController.navigate(route = SalesDestinations.SALES_SEARCH_STOCK_ROUTE)
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE) {
            showBottomMenu(true)
            ProductListScreen(
                isEditable = false,
                categoryId = 0L,
                onItemClick = { productId ->
                    navController.navigate(route = "${SalesDestinations.SALES_NEW_SALE_ORDERS_ROUTE}$productId")
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_NEW_SALE_ORDERS_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                SaleScreen(
                    isEdit = false,
                    screenTitle = stringResource(id = R.string.new_sale_label),
                    isOrderedByCustomer = true,
                    productId = productId.toLong(),
                    saleId = 0L,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = SalesDestinations.SALES_NEW_SALE_STOCK_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { stockItemId ->
                SaleScreen(
                    isEdit = false,
                    screenTitle = stringResource(id = R.string.new_sale_label),
                    isOrderedByCustomer = false,
                    productId = stockItemId.toLong(),
                    saleId = 0L,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = SalesDestinations.SALES_EDIT_SALE_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { saleItemId ->
                SaleScreen(
                    isEdit = true,
                    screenTitle = stringResource(id = R.string.edit_sale_label),
                    isOrderedByCustomer = true,
                    productId = 0L,
                    saleId = saleItemId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object SalesDestinations {
    const val MAIN_SALES_ROUTE = "main_sales_route"
    const val SALES_OPTIONS_ROUTE = "sales_options_route"
    const val SALES_STOCK_LIST_ROUTE = "sales_stock_list_route"
    const val SALES_SEARCH_STOCK_ROUTE = "sales_search_stock_route"
    const val SALES_PRODUCTS_LIST_ROUTE = "sales_products_list_route"
    const val SALES_NEW_SALE_ORDERS_ROUTE = "sales_new_sale_orders_route"
    const val SALES_NEW_SALE_ORDERS_WITH_ID_ROUTE = "$SALES_NEW_SALE_ORDERS_ROUTE{$ITEM_ID}"
    const val SALES_NEW_SALE_STOCK_ROUTE = "sales_new_sale_stock_route"
    const val SALES_NEW_SALE_STOCK_WITH_ID_ROUTE = "$SALES_NEW_SALE_STOCK_ROUTE{$ITEM_ID}"
    const val SALES_EDIT_SALE_ROUTE = "sales_edit_sale_route"
    const val SALES_EDIT_SALE_WITH_ID_ROUTE = "$SALES_EDIT_SALE_ROUTE{$ITEM_ID}"
}