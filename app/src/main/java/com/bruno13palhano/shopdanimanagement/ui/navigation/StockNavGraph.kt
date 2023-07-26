package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockCategoriesScreen

private const val CATEGORY_ID = "categoryId"

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
                onMenuClick = onMenuClick
            )
        }
        composable(route = StockDestinations.STOCK_CATEGORIES_ROUTE) {
            StockCategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(route = "${StockDestinations.STOCK_PRODUCT_LIST_ROUTE}$categoryId")
                },
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(route = StockDestinations.STOCK_PRODUCT_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(CATEGORY_ID)?.let { categoryId ->
                ProductListScreen(
                    categoryId = categoryId,
                    onAddButtonClick = {

                    },
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

object StockDestinations {
    const val MAIN_STOCK_ROUTE = "main_stock_route"
    const val STOCK_CATEGORIES_ROUTE = "stock_categories_route"
    const val STOCK_PRODUCT_LIST_ROUTE = "stock_product_list_route/"
    const val STOCK_PRODUCT_LIST_WITH_ID_ROUTE = "$STOCK_PRODUCT_LIST_ROUTE{$CATEGORY_ID}"
}