package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesSearchProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewOrderSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.NewStockSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.SalesStockRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.salesNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Sales>(startDestination = SalesRoutes.Main) {
        composable<SalesRoutes.Main> {
            SalesRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId, productId ->
                    navController.navigate(
                        route = SalesRoutes.EditSale(saleId = saleId, productId = productId)
                    )
                },
                onAddButtonClick = { navController.navigate(route = SalesRoutes.Options) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.Options> {
            SalesOptionsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onOrdersOptionClick = { navController.navigate(route = SalesRoutes.SaleProducts) },
                onStockOptionClick = { navController.navigate(route = SalesRoutes.Stock) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.SearchStock> {
            StockSearchRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { itemId ->
                    navController.navigate(
                        route = SalesRoutes.NewStockSale(itemId = itemId)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.Stock> {
            SalesStockRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { itemId ->
                    navController.navigate(
                        route = SalesRoutes.NewStockSale(itemId)
                    )
                },
                onSearchClick = { navController.navigate(route = SalesRoutes.SearchStock) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.SaleProducts> {
            SalesProductListRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = SalesRoutes.NewOrderSale(productId = productId)
                    )
                },
                onSearchClick = { navController.navigate(route = SalesRoutes.SearchProduct) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.SearchProduct> {
            SalesSearchProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = SalesRoutes.NewOrderSale(productId = productId)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.NewStockSale> { backStackEntry ->
            val id = backStackEntry.toRoute<SalesRoutes.NewOrderSale>().productId

            NewStockSaleRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                productId = id,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.NewOrderSale> { backStackEntry ->
            val id = backStackEntry.toRoute<SalesRoutes.NewOrderSale>().productId

            NewOrderSaleRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                productId = id,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<SalesRoutes.EditSale> { backStackEntry ->
            val saleId = backStackEntry.toRoute<SalesRoutes.EditSale>().saleId
            val productId = backStackEntry.toRoute<SalesRoutes.EditSale>().productId

            EditSaleRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                saleId = saleId,
                productId = productId,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface SalesRoutes {
    @Serializable
    object Main

    @Serializable
    object Options

    @Serializable
    object Stock

    @Serializable
    object SearchStock

    @Serializable
    object SaleProducts

    @Serializable
    object SearchProduct

    @Serializable
    data class NewStockSale(val itemId: Long)

    @Serializable
    data class NewOrderSale(val productId: Long)

    @Serializable
    data class EditSale(val saleId: Long, val productId: Long)
}