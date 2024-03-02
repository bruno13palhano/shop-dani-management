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
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.EditStockItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.NewStockItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchRoute

fun NavGraphBuilder.stockNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = StockDestinations.STOCK_MAIN_ROUTE,
        route = HomeDestinations.HOME_STOCK_ROUTE
    ) {
        composable(route = StockDestinations.STOCK_MAIN_ROUTE) {
            StockRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_ITEM_ROUTE}/$stockId"
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
            StockSearchRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_EDIT_ITEM_ROUTE}/$stockId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_ITEM_LIST_ROUTE) {
            SalesProductListRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_NEW_ITEM_ROUTE}/$productId"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE) {
            SalesSearchProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_NEW_ITEM_ROUTE}/$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${StockDestinations.STOCK_NEW_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { productId ->
                NewStockItemRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    productId = productId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${StockDestinations.STOCK_EDIT_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { stockItemId ->
                EditStockItemRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    stockItemId = stockItemId,
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
    const val STOCK_SEARCH_PRODUCT_ROUTE = "stock_search_product_route"
    const val STOCK_NEW_ITEM_ROUTE = "stock_new_item_route"
    const val STOCK_EDIT_ITEM_ROUTE = "stock_edit_item_route"
}