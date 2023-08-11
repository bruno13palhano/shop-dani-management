package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.ShoppingScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.shoppingNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit,
) {
    navigation(
        startDestination = ShoppingDestinations.MAIN_SHOPPING_ROUTE,
        route = MainDestinations.SHOPPING_ROUTE,
    ) {
        composable(route = ShoppingDestinations.MAIN_SHOPPING_ROUTE) {
            ShoppingScreen(
                onMenuClick = onMenuClick,
                onCategoriesClick = {
                    navController.navigate(route = ShoppingDestinations.SHOPPING_CATEGORIES_ROUTE)
                }
            )
        }
        composable(route = ShoppingDestinations.SHOPPING_CATEGORIES_ROUTE) {
            CategoriesScreen(
                isOrderedByCustomer = false,
                onItemClick = { categoryId ->
                    navController.navigate(route = "${ShoppingDestinations.SHOPPING_LIST_ROUTE}$categoryId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ShoppingDestinations.SHOPPING_LIST_WITH_ID_ROUTE) { backStackEntry ->
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
                    categoryId = categoryId.toLong(),
                    onItemClick = { productId ->

                    },
                    onAddButtonClick = {

                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object ShoppingDestinations {
    const val MAIN_SHOPPING_ROUTE = "main_shopping_route"
    const val SHOPPING_CATEGORIES_ROUTE = "shopping_categories_route"
    const val SHOPPING_LIST_ROUTE = "shopping_list_route/"
    const val SHOPPING_LIST_WITH_ID_ROUTE = "$SHOPPING_LIST_ROUTE{$ITEM_ID}"
    const val SHOPPING_PRODUCT_ROUTE = "shopping_product_route"
}