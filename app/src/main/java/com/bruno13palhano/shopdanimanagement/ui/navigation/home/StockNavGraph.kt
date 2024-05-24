package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesProductListRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.products.SalesSearchProductRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.EditStockItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.NewStockItemRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.StockSearchRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.stockNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Stock>(startDestination = StockRoutes.Main) {
        composable<StockRoutes.Main> {
            StockRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockItemId ->
                    navController.navigate(route = StockRoutes.EditItem(stockItemId = stockItemId))
                },
                onSearchClick = { navController.navigate(route = StockRoutes.SearchItem) },
                onAddButtonClick = { navController.navigate(route = StockRoutes.ItemList) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable<StockRoutes.SearchItem> {
            StockSearchRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockItemId ->
                    navController.navigate(route = StockRoutes.EditItem(stockItemId = stockItemId))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<StockRoutes.ItemList> {
            SalesProductListRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(
                        route = StockRoutes.NewItem(productId = productId)
                    )
                },
                onSearchClick = { navController.navigate(route = StockRoutes.SearchProduct) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<StockRoutes.SearchProduct> {
            SalesSearchProductRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { productId ->
                    navController.navigate(route = StockRoutes.NewItem(productId = productId))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<StockRoutes.NewItem> { backStackEntry ->
            val productId = backStackEntry.toRoute<StockRoutes.NewItem>().productId

            NewStockItemRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                productId = productId,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<StockRoutes.EditItem> { backStackEntry ->
            val stockItemId = backStackEntry.toRoute<StockRoutes.EditItem>().stockItemId


            EditStockItemRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                stockItemId = stockItemId,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface StockRoutes {
    @Serializable
    object Main

    @Serializable
    object SearchItem

    @Serializable
    object ItemList

    @Serializable
    object SearchProduct

    @Serializable
    data class NewItem(val productId: Long)

    @Serializable
    data class EditItem(val stockItemId: Long)
}