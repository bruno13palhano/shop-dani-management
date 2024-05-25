package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CanceledSalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CustomersDebitRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialInfoRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.StockDebitsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.FinancialEditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.FinancialEditStockItemRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.financialNavGraph(
    sharedTransitionScope: SharedTransitionScope,
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation<MainRoutes.Financial>(startDestination = FinancialRoutes.Main) {
        composable<FinancialRoutes.Main> {
            FinancialRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { route ->
                    navController.navigate(route)
                },
                onIconMenuClick = onIconMenuClick,
                goHome = {
                    navController.navigate(route = HomeRoutes.Main) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<FinancialRoutes.Info> {
            FinancialInfoRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<FinancialRoutes.CanceledSales> {
            CanceledSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<FinancialRoutes.StockDebits> {
            StockDebitsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockItemId ->
                    navController.navigate(
                        route = FinancialRoutes.StockItem(id = stockItemId)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<FinancialRoutes.CustomersDebits> {
            CustomersDebitRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId, productId ->
                    navController.navigate(
                        route = FinancialRoutes.SaleItem(
                            saleId = saleId, productId = productId
                        )
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<FinancialRoutes.SaleItem> { backStackEntry ->
            val saleId = backStackEntry.toRoute<FinancialRoutes.SaleItem>().saleId
            val productId = backStackEntry.toRoute<FinancialRoutes.SaleItem>().productId

            FinancialEditSaleRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                saleId = saleId,
                productId = productId,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<FinancialRoutes.StockItem> { backStackEntry ->
            val stockItemId = backStackEntry.toRoute<FinancialRoutes.StockItem>().id

            FinancialEditStockItemRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                stockItemId = stockItemId,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface FinancialRoutes {
    @Serializable
    data object Main: FinancialRoutes

    @Serializable
    data object Info: FinancialRoutes

    @Serializable
    data object CanceledSales: FinancialRoutes

    @Serializable
    data object CustomersDebits: FinancialRoutes

    @Serializable
    data object StockDebits: FinancialRoutes

    @Serializable
    data class SaleItem(val saleId: Long, val productId: Long): FinancialRoutes

    @Serializable
    data class StockItem(val id: Long): FinancialRoutes
}