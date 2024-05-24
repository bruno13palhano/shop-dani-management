package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.NewCatalogItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.EditProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.NewProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductCategoriesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.ProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SearchProductRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.productsNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation<MainRoutes.Products>(startDestination = ProductsRoutes.Main) {
        composable<ProductsRoutes.Main> {
            ProductCategoriesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { categoryId ->
                    navController.navigate(route = ProductsRoutes.List(categoryId = categoryId))
                },
                onIconMenuClick = onIconMenuClick
            )
        }

        composable<ProductsRoutes.List> { backStackEntry ->
            val categoryId = backStackEntry.toRoute<ProductsRoutes.List>().categoryId

            ProductListRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                categoryId = categoryId,
                onItemClick = { productId ->
                    navController.navigate(route = ProductsRoutes.EditProduct(id = productId))
                },
                onSearchClick = {
                    navController.navigate(route = ProductsRoutes.Search(categoryId = categoryId))
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = ProductsRoutes.NewProduct(categoryId = categoryId)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<ProductsRoutes.Search> { backStackEntry ->
            val categoryId = backStackEntry.toRoute<ProductsRoutes.Search>().categoryId

            SearchProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                categoryId = categoryId,
                onItemClick = { productId ->
                    navController.navigate(route = ProductsRoutes.EditProduct(id = productId))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<ProductsRoutes.CatalogNewItem> { backStackEntry ->
            val productId = backStackEntry.toRoute<ProductsRoutes.CatalogNewItem>().productId

            NewCatalogItemRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                productId = productId,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<ProductsRoutes.NewProduct> { backStackEntry ->
            val categoryId = backStackEntry.toRoute<ProductsRoutes.NewProduct>().categoryId

            NewProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                categoryId = categoryId,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<ProductsRoutes.EditProduct> { backStackEntry ->
            val productId = backStackEntry.toRoute<ProductsRoutes.EditProduct>().id

            EditProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                productId = productId,
                onAddToCatalogClick = { id ->
                    navController.navigate(route = ProductsRoutes.CatalogNewItem(productId = id))
                },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface ProductsRoutes {
    @Serializable
    object Main

    @Serializable
    data class List(val categoryId: Long)

    @Serializable
    data class Search(val categoryId: Long)

    @Serializable
    data class NewProduct(val categoryId: Long)

    @Serializable
    data class EditProduct(val id: Long)

    @Serializable
    data class CatalogNewItem(val productId: Long)
}