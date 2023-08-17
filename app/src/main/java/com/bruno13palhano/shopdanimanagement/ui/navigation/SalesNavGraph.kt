package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.SalesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleProductsScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.salesNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = SalesDestinations.MAIN_SALES_ROUTE,
        route = MainDestinations.SALES_ROUTE
    ) {
        composable(route = SalesDestinations.MAIN_SALES_ROUTE) {
            SalesScreen(
                onItemClick = { saleItemId ->
                    navController.navigate(route = "${SalesDestinations.SALES_EDIT_SALE_ROUTE}$saleItemId")
                },
                onMenuClick = onMenuClick,
                onAddButtonClick = {
                    navController.navigate(route = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE)
                }
            )
        }
        composable(route = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE) {
            SaleProductsScreen(
                onItemClick = { productId ->
                    navController.navigate(route = "${SalesDestinations.SALES_NEW_SALE_ROUTE}$productId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = SalesDestinations.SALES_NEW_SALE_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                SaleScreen(
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = SalesDestinations.SALES_EDIT_SALE_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { saleItemId ->

            }
        }
    }
}

object SalesDestinations {
    const val MAIN_SALES_ROUTE = "main_sales_route"
    const val SALES_PRODUCTS_LIST_ROUTE = "sales_products_list_route"
    const val SALES_PRODUCTS_LIST_WITH_ID_ROUTE = "$SALES_PRODUCTS_LIST_ROUTE{$ITEM_ID}"
    const val SALES_NEW_SALE_ROUTE = "sales_new_sale_route"
    const val SALES_NEW_SALE_WITH_ID_ROUTE = "$SALES_NEW_SALE_ROUTE{$ITEM_ID}"
    const val SALES_EDIT_SALE_ROUTE = "sales_edit_sale_route"
    const val SALES_EDIT_SALE_WITH_ID_ROUTE = "$SALES_EDIT_SALE_ROUTE{$ITEM_ID}"
}