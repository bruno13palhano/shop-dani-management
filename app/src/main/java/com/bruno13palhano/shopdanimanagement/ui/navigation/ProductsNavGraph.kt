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
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.NewCatalogItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.EditProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.NewProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductCategoriesScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductScreen

fun NavGraphBuilder.productsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = ProductsDestinations.MAIN_PRODUCTS_ROUTE,
        route = MainDestinations.PRODUCTS_ROUTE
    ) {
        composable(route = ProductsDestinations.MAIN_PRODUCTS_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            ProductCategoriesScreen(
                onItemClick = { categoryId ->
                    navController.navigate(
                        route = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}/$categoryId"
                    )
                },
                onIconMenuClick = onIconMenuClick
            )
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { categoryId ->
                ProductListScreen(
                    isEditable = true,
                    categoryId = categoryId,
                    onItemClick = { productId ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}/$productId"
                        )
                    },
                    onSearchClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_SEARCH_ROUTE}/$categoryId"
                        )
                    },
                    onAddButtonClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_NEW_PRODUCT_ROUTE}/$categoryId"
                        )
                    },
                    showBottomMenu = showBottomMenu,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_SEARCH_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { categoryId ->
                SearchProductScreen(
                    isEditable = true,
                    categoryId = categoryId,
                    onItemClick = { productId ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}/$productId"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { productId ->
                NewCatalogItemRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    productId = productId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_PRODUCT_ROUTE}/{$ITEM_ID}/{$EDITABLE}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(EDITABLE) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)
            val editable = backStackEntry.arguments?.getBoolean(EDITABLE)

            if (id != null && editable != null) {
                ProductScreen(
                    isEditable = editable,
                    screenTitle = if (editable) {
                        stringResource(id = R.string.edit_product_label)
                    } else {
                        stringResource(id = R.string.new_product_label)
                    },
                    categoryId = if (editable) 0L else id,
                    productId = if (editable) id else 0L,
                    onAddToCatalogClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/${id}"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_NEW_PRODUCT_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { categoryId ->
                NewProductRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    categoryId = categoryId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_EDIT_PRODUCT_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { productId ->
                EditProductRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    productId = productId,
                    onAddToCatalogClick = {
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/$productId"
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
    const val PRODUCTS_PRODUCT_ROUTE = "products_product_route"
    const val PRODUCTS_NEW_PRODUCT_ROUTE = "products_new_product_route"
    const val PRODUCTS_EDIT_PRODUCT_ROUTE = "products_edit_product_route"
    const val PRODUCTS_CATALOG_NEW_ITEM_ROUTE = "products_catalog_new_item_route"
}