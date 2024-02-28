package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.NewCatalogItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.EditProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.NewProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductCategoriesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductRoute

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
            ProductCategoriesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
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
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { categoryId ->
                ProductListRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
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
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${ProductsDestinations.PRODUCTS_SEARCH_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { categoryId ->
                SearchProductRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
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
            route = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/{$ITEM_ID}",
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
                    onAddToCatalogClick = { id ->
                        navController.navigate(
                            route = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/$id"
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
    const val PRODUCTS_NEW_PRODUCT_ROUTE = "products_new_product_route"
    const val PRODUCTS_EDIT_PRODUCT_ROUTE = "products_edit_product_route"
    const val PRODUCTS_CATALOG_NEW_ITEM_ROUTE = "products_catalog_new_item_route"
}