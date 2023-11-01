package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.ItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrderSearchScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.StockOrdersScreen

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
                onItemClick = { stockId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_ITEM_ROUTE}/$stockId/${true}/${false}"
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
                onItemClick = { stockId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_ITEM_ROUTE}/$stockId/${true}/${false}"
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
                        route = "${StockDestinations.STOCK_ITEM_ROUTE}/$productId/${false}/${false}"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE
                    )
                },
                onAddButtonClick = {},
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE) {
            showBottomMenu(true)
            SearchProductScreen(
                isEditable = false,
                categoryId = 0L,
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${StockDestinations.STOCK_ITEM_ROUTE}/$productId/${false}/${false}"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${StockDestinations.STOCK_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(EDITABLE) { type = NavType.BoolType },
                navArgument(IS_ORDERED) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)
            val editable = backStackEntry.arguments?.getBoolean(EDITABLE)
            val isOrdered = backStackEntry.arguments?.getBoolean(IS_ORDERED)

            if (id != null && editable != null && isOrdered != null) {
                ItemScreen(
                    isEditable = editable,
                    stockOrderItemId = if (editable) id else 0L,
                    isOrderedByCustomer = isOrdered,
                    screenTitle = if (editable) {
                        stringResource(id = R.string.edit_stock_item_label)
                    } else {
                        stringResource(id = R.string.new_stock_item_label)
                    },
                    productId = if (editable) 0L else id,
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
    const val STOCK_ITEM_ROUTE = "stock_item_route"
}