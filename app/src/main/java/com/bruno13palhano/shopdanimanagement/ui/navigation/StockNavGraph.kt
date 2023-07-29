package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.StockScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.NewProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockListScreen
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
        val bottomMenuNavActions = BottomMenuNavActions(navController)
        composable(route = StockDestinations.MAIN_STOCK_ROUTE) {
            StockScreen(
                destinationHierarchy = it.destination.hierarchy,
                onBottomMenuItemClick = { route ->
                    bottomMenuNavActions.navigateFromBottomMenu(route)
                },
                onClick = { route ->
                    navController.navigate(route)
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
        composable(route = StockDestinations.STOCK_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(CATEGORY_ID)?.let { categoryId ->
                StockListScreen(
                    categoryId = categoryId,
                    onAddButtonClick = {
                        navController.navigate(route = "${StockDestinations.STOCK_PRODUCT_ROUTE}$categoryId")
                    },
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
        composable(route = StockDestinations.STOCK_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(CATEGORY_ID)?.let { categoryId ->
                NewProductScreen(
                    categoryId = categoryId,
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
    const val STOCK_LIST_ROUTE = "stock_list_route/"
    const val STOCK_LIST_WITH_ID_ROUTE = "$STOCK_LIST_ROUTE{$CATEGORY_ID}"
    const val STOCK_PRODUCT_ROUTE = "stock_product_route"
    const val STOCK_PRODUCT_WITH_ID_ROUTE = "$STOCK_PRODUCT_ROUTE{$CATEGORY_ID}"
}