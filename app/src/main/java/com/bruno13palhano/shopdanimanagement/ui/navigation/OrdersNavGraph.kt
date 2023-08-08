package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.OrdersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.EditProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.NewProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.SearchProductScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.ordersBavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.MAIN_ORDERS_ROUTE,
        route = MainDestinations.ORDERS_ROUTE
    ) {
        composable(route = OrdersDestinations.MAIN_ORDERS_ROUTE) {
            OrdersScreen(
                onSearchClick = {
                    navController.navigate(OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE)
                },
                onMenuClick = onMenuClick,
                onCategoriesClick = {
                    navController.navigate(route = OrdersDestinations.ORDERS_CATEGORIES_ROUTE)
                }
            )
        }
        composable(route = OrdersDestinations.ORDERS_CATEGORIES_ROUTE) {
            CategoriesScreen(
                isOrderedByCustomer = true,
                onItemClick = { categoryId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_LIST_ROUTE}$categoryId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE) {
            SearchProductScreen(
                isOrderedByCustomer = true,
                onItemClick = { productId ->
                    navController.navigate(route = "${OrdersDestinations.ORDERS_SEARCH_PRODUCT_ROUTE}$productId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = OrdersDestinations.ORDERS_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
                    categoryId = categoryId.toLong(),
                    isOrderedByCustomer = true,
                    onItemClick = { productId ->
                        navController.navigate(route = "${OrdersDestinations.ORDERS_EDIT_PRODUCT_ROUTE}$productId")
                    },
                    onAddButtonClick = {
                        navController.navigate(route = "${OrdersDestinations.ORDERS_NEW_PRODUCT_ROUTE}$categoryId")
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = OrdersDestinations.ORDERS_NEW_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                NewProductScreen(
                    categoryId = categoryId.toLong(),
                    isOrderedByCustomer = true,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = OrdersDestinations.ORDERS_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                EditProductScreen(
                    productId = productId.toLong(),
                    isOrderedByCustomer = true,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object OrdersDestinations {
    const val MAIN_ORDERS_ROUTE = "main_orders_route"
    const val ORDERS_CATEGORIES_ROUTE = "orders_categories_route"
    const val ORDERS_SEARCH_PRODUCT_ROUTE = "orders_search_product_route"
    const val ORDERS_LIST_ROUTE = "orders_list_route/"
    const val ORDERS_LIST_WITH_ID_ROUTE = "$ORDERS_LIST_ROUTE{$ITEM_ID}"
    const val ORDERS_NEW_PRODUCT_ROUTE = "orders_new_product_route"
    const val ORDERS_NEW_PRODUCT_WITH_ID_ROUTE = "$ORDERS_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val ORDERS_EDIT_PRODUCT_ROUTE = "orders_edit_product_route"
    const val ORDERS_EDIT_PRODUCT_WITH_ID_ROUTE = "$ORDERS_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}