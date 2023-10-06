package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.CatalogItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductCategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen

private const val ITEM_ID = "item_Id"
private const val ITEM_EDITABLE = "item_editable"

fun NavGraphBuilder.productsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = ProductsDestinations.MAIN_PRODUCTS_ROUTE,
        route = MainDestinations.PRODUCTS_ROUTE
    ) {
        composable(route = ProductsDestinations.MAIN_PRODUCTS_ROUTE) {
            showBottomMenu(true)
            ProductCategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(
                        route = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}$categoryId"
                    )
                },
                onIconMenuClick = onIconMenuClick
            )
        }
        composable(route = ProductsDestinations.PRODUCTS_LIST_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
                    isEditable = true,
                    categoryId = categoryId.toLong(),
                    onItemClick = { productId ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}$productId"
                        )
                    },
                    onSearchClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_SEARCH_ROUTE}$categoryId"
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
        composable(route = ProductsDestinations.PRODUCTS_SEARCH_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                SearchProductScreen(
                    isEditable = true,
                    categoryId = categoryId.toLong(),
                    onItemClick = { productId ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}$productId"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = ProductsDestinations.PRODUCTS_NEW_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { categoryId ->
                ProductScreen(
                    isEditable = false,
                    screenTitle = stringResource(id = R.string.new_product_label),
                    categoryId = categoryId.toLong(),
                    productId = 0L,
                    onAddToCatalogClick = {},
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = ProductsDestinations.PRODUCTS_CATALOG_ITEM_WITH_ID_ROUTE,
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(ITEM_EDITABLE) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)
            val editable = backStackEntry.arguments?.getBoolean(ITEM_EDITABLE)
            if (id != null && editable != null) {
                CatalogItemScreen(
                    productId = id,
                    catalogId = 0L,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = ProductsDestinations.PRODUCTS_EDIT_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                ProductScreen(
                    isEditable = true,
                    screenTitle = stringResource(id = R.string.edit_product_label),
                    categoryId = 0L,
                    productId = productId.toLong(),
                    onAddToCatalogClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_CATALOG_ITEM_ROUTE}${productId}${false}"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object ProductsDestinations {
    const val MAIN_PRODUCTS_ROUTE = "main_products_route"
    const val PRODUCTS_LIST_ROUTE = "products_list_route"
    const val PRODUCTS_SEARCH_ROUTE = "products_search_route"
    const val PRODUCTS_SEARCH_WITH_ID_ROUTE = "$PRODUCTS_SEARCH_ROUTE{${ITEM_ID}}"
    const val PRODUCTS_LIST_WITH_ID_ROUTE = "$PRODUCTS_LIST_ROUTE{$ITEM_ID}"
    const val PRODUCTS_NEW_PRODUCT_ROUTE = "products_new_product_route"
    const val PRODUCTS_NEW_PRODUCT_WITH_ID_ROUTE = "$PRODUCTS_NEW_PRODUCT_ROUTE{$ITEM_ID}"
    const val PRODUCTS_EDIT_PRODUCT_ROUTE = "products_edit_product_route"
    const val PRODUCTS_EDIT_PRODUCT_WITH_ID_ROUTE = "$PRODUCTS_EDIT_PRODUCT_ROUTE{$ITEM_ID}"
    const val PRODUCTS_CATALOG_ITEM_ROUTE = "products_catalog_item_route"
    const val PRODUCTS_CATALOG_ITEM_WITH_ID_ROUTE = "$PRODUCTS_CATALOG_ITEM_ROUTE{$ITEM_ID}{$ITEM_EDITABLE}"
}