package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.EditProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.NewProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.SearchProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockCategoriesScreen

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
                onClick = { route ->
                    navController.navigate(route)
                },
                onSearchClick = {
                    navController.navigate(StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = StockDestinations.STOCK_CATEGORIES_ROUTE) {
            StockCategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(route = "${StockDestinations.STOCK_LIST_ROUTE}$categoryId")
                },
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE) {
            SearchProductScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                StockListScreen(
                    categoryId = categoryId.toLong(),
                    onItemClick = { productId ->
                        navController.navigate(route = "${StockDestinations.STOCK_EDIT_PRODUCT_ROUTE}$productId")
                    },
                    onAddButtonClick = {
                        navController.navigate(route = "${StockDestinations.STOCK_NEW_PRODUCT_ROUTE}$categoryId")
                    },
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
        composable(route = StockDestinations.STOCK_NEW_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                NewProductScreen(
                    categoryId = categoryId.toLong(),
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
        composable(route = StockDestinations.STOCK_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                EditProductScreen(
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object StockDestinations {
    const val MAIN_STOCK_ROUTE = "main_stock_route"
    const val STOCK_CATEGORIES_ROUTE = "stock_categories_route"
    const val STOCK_SEARCH_PRODUCT_ROUTE = "stock_search_product"
    const val STOCK_LIST_ROUTE = "stock_list_route/"
    const val STOCK_LIST_WITH_ID_ROUTE = "$STOCK_LIST_ROUTE{$ITEM_ID}"
    const val STOCK_NEW_PRODUCT_ROUTE = "stock_new_product_route"
    const val STOCK_NEW_PRODUCT_WITH_ID_ROUTE = "$STOCK_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val STOCK_EDIT_PRODUCT_ROUTE = "stock_edit_product_route"
    const val STOCK_EDIT_PRODUCT_WITH_ID_ROUTE = "$STOCK_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}