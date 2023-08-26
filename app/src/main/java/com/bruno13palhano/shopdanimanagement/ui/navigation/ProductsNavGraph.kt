package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.EditProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.NewProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductsScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.productsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = ProductsDestinations.MAIN_PRODUCTS_ROUTE,
        route = MainDestinations.PRODUCTS_ROUTE
    ) {
        composable(route = ProductsDestinations.MAIN_PRODUCTS_ROUTE) {
            showBottomMenu(false)
            ProductsScreen(
                onCategoriesClick = {
                    navController.navigate(route = ProductsDestinations.PRODUCTS_CATEGORIES_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ProductsDestinations.PRODUCTS_CATEGORIES_ROUTE) {
            showBottomMenu(false)
            CategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(route = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}$categoryId")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ProductsDestinations.PRODUCTS_LIST_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
                    categoryId = categoryId.toLong(),
                    onItemClick = { productId ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}$productId"
                        )
                    },
                    onAddButtonClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_NEW_PRODUCT_ROUTE}$categoryId"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = ProductsDestinations.PRODUCTS_NEW_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                NewProductScreen(
                    categoryId = categoryId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = ProductsDestinations.PRODUCTS_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                EditProductScreen(
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object ProductsDestinations {
    const val MAIN_PRODUCTS_ROUTE = "main_products_route"
    const val PRODUCTS_CATEGORIES_ROUTE = "products_categories_route"
    const val PRODUCTS_LIST_ROUTE = "products_list_route/"
    const val PRODUCTS_LIST_WITH_ID_ROUTE = "$PRODUCTS_LIST_ROUTE{$ITEM_ID}"
    const val PRODUCTS_NEW_PRODUCT_ROUTE = "products_new_product_route"
    const val PRODUCTS_NEW_PRODUCT_WITH_ID_ROUTE = "$PRODUCTS_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val PRODUCTS_EDIT_PRODUCT_ROUTE = "products_edit_product_route"
    const val PRODUCTS_EDIT_PRODUCT_WITH_ID_ROUTE = "$PRODUCTS_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
}