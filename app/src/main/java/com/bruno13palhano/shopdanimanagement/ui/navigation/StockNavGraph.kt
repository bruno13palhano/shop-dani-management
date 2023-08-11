package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.EditProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.NewProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen

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
                onCategoriesClick = {
                    navController.navigate(StockDestinations.STOCK_CATEGORIES_ROUTE)
                },
                onSearchClick = {
                    navController.navigate(StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE)
                },
                onMenuClick = onMenuClick
            )
        }
        composable(route = StockDestinations.STOCK_CATEGORIES_ROUTE) {
            CategoriesScreen(
                isOrderedByCustomer = false,
                onItemClick = { categoryId ->
                    navController.navigate(route = "${StockDestinations.STOCK_LIST_ROUTE}$categoryId")
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(route = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE) {
            SearchProductScreen(
                onItemClick = { productId ->
                    navController.navigate(route = "${StockDestinations.STOCK_EDIT_PRODUCT_ROUTE}$productId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = StockDestinations.STOCK_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
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

            }
        }
        composable(route = StockDestinations.STOCK_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->

            }
        }
    }
}

object StockDestinations {
    const val MAIN_STOCK_ROUTE = "main_stock_route"
    const val STOCK_CATEGORIES_ROUTE = "stock_categories_route"
    const val STOCK_SEARCH_PRODUCT_ROUTE = "stock_search_product_route"
    const val STOCK_LIST_ROUTE = "stock_list_route/"
    const val STOCK_LIST_WITH_ID_ROUTE = "$STOCK_LIST_ROUTE{$ITEM_ID}"
    const val STOCK_NEW_PRODUCT_ROUTE = "stock_new_product_route"
    const val STOCK_NEW_PRODUCT_WITH_ID_ROUTE = "$STOCK_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val STOCK_EDIT_PRODUCT_ROUTE = "stock_edit_product_route"
    const val STOCK_EDIT_PRODUCT_WITH_ID_ROUTE = "$STOCK_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}