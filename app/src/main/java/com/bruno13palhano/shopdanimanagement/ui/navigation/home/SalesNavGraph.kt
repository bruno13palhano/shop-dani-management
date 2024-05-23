package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.PRODUCT_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.SALE_ID
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesSearchProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewOrderSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewStockSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.SalesStockRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchRoute

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.salesNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = SalesDestinations.MAIN_SALES_ROUTE,
        route = HomeDestinations.HOME_SALES_ROUTE
    ) {
        composable(route = SalesDestinations.MAIN_SALES_ROUTE) {
            SalesRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId, productId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}/$saleId/$productId"
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
                    productId = id,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = this@composable,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${SalesDestinations.SALES_NEW_ORDER_SALE_ROUTE}/{$PRODUCT_ID}",
            arguments = listOf(navArgument(PRODUCT_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(PRODUCT_ID)?.let { id ->
                NewOrderSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    productId = id,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = this@composable,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}/{$SALE_ID}/{$PRODUCT_ID}",
            arguments = listOf(
                navArgument(SALE_ID) { type = NavType.LongType },
                navArgument(PRODUCT_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getLong(SALE_ID)
            val productId = backStackEntry.arguments?.getLong(PRODUCT_ID)

            if (saleId != null && productId != null) {
                EditSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    saleId = saleId,
                    productId = productId,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = this@composable,
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