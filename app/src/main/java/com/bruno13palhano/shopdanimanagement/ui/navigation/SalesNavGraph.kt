package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsScreen

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
            showBottomMenu(true)
            gesturesEnabled(true)
            SalesScreen(
                isOrders = false,
                screenTitle = stringResource(id = R.string.sales_label),
                onItemClick = { saleId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_SALE_ROUTE}/$saleId/${true}/${false}"
                    )
                },
                onAddButtonClick = {
                    navController.navigate(route = SalesDestinations.SALES_OPTIONS_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_OPTIONS_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
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
            gesturesEnabled(true)
            StockSearchScreen(
                onItemClick = { stockItem ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_SALE_ROUTE}/$stockItem/${true}/${false}"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_STOCK_LIST_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            StockScreen(
                isAddButtonEnabled = false,
                screenTitle = stringResource(id = R.string.stock_list_label),
                onItemClick = { itemId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_SALE_ROUTE}/$itemId/${false}/${false}"
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
            gesturesEnabled(true)
            ProductListScreen(
                isEditable = false,
                categoryId = 0L,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_SALE_ROUTE}/$productId/${false}/${true}"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = SalesDestinations.SALES_SEARCH_PRODUCT_ROUTE
                    )
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_SEARCH_PRODUCT_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            SearchProductScreen(
                isEditable = false,
                categoryId = 0L,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${SalesDestinations.SALES_SALE_ROUTE}/$productId/${false}/${true}"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(EDITABLE) { type = NavType.BoolType },
                navArgument(IS_ORDERED) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)
            val editable = backStackEntry.arguments?.getBoolean(EDITABLE)
            val isOrdered = backStackEntry.arguments?.getBoolean(IS_ORDERED)

            if (id != null && editable != null && isOrdered != null) {
                SaleScreen(
                    isEdit = editable,
                    screenTitle = if (editable) {
                        stringResource(id = R.string.edit_sale_label)
                    } else {
                        stringResource(id = R.string.new_sale_label)
                    },
                    isOrderedByCustomer = isOrdered,
                    stockOrderId = if (editable) 0L else id,
                    saleId = if (editable) id else 0L,
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
}