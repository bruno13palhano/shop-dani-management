package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesSearchProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewOrderSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewStockSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.SalesStockRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchRoute

fun NavGraphBuilder.salesNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = SalesDestinations.MAIN_SALES_ROUTE,
        route = HomeDestinations.HOME_SALES_ROUTE
    ) {
        composable(route = SalesDestinations.MAIN_SALES_ROUTE) {
            SalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}/$saleId"
                    )
                },
                onAddButtonClick = {
                    navController.navigate(route = SalesDestinations.SALES_OPTIONS_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_OPTIONS_ROUTE) {
            SalesOptionsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
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
            StockSearchRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockItem ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_NEW_STOCK_SALE_ROUTE}/$stockItem"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_STOCK_LIST_ROUTE) {
            SalesStockRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { itemId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_NEW_STOCK_SALE_ROUTE}/$itemId"
                    )
                },
                onSearchClick = {
                    navController.navigate(route = SalesDestinations.SALES_SEARCH_STOCK_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE) {
            SalesProductListRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_NEW_ORDER_SALE_ROUTE}/$productId"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = SalesDestinations.SALES_SEARCH_PRODUCT_ROUTE
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_SEARCH_PRODUCT_ROUTE) {
            SalesSearchProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_NEW_ORDER_SALE_ROUTE}/$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${SalesDestinations.SALES_NEW_STOCK_SALE_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { id ->
                NewStockSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    stockOrderId = id,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${SalesDestinations.SALES_NEW_ORDER_SALE_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { id ->
                NewOrderSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    productId = id,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}/{$ITEM_ID}",
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

object SalesDestinations {
    const val MAIN_SALES_ROUTE = "main_sales_route"
    const val SALES_OPTIONS_ROUTE = "sales_options_route"
    const val SALES_STOCK_LIST_ROUTE = "sales_stock_list_route"
    const val SALES_SEARCH_STOCK_ROUTE = "sales_search_stock_route"
    const val SALES_PRODUCTS_LIST_ROUTE = "sales_products_list_route"
    const val SALES_SEARCH_PRODUCT_ROUTE = "sales_search_product_route"
    const val SALES_SALE_ROUTE = "sales_sale_route"
    const val SALES_NEW_STOCK_SALE_ROUTE = "sales_new_stock_sale_route"
    const val SALES_NEW_ORDER_SALE_ROUTE = "sales_new_order_sale_route"
    const val SALES_EDIT_SALE_ROUTE = "sales_edit_sale_route"
}