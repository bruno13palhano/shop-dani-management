package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CanceledSalesRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CustomersDebitRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialInfoRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.StockDebitsRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.FinancialEditSaleRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.FinancialEditStockItemRoute

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.financialNavGraph(
    sharedTransitionScope: SharedTransitionScope,
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = FinancialDestinations.FINANCIAL_MAIN_ROUTE,
        route = MainDestinations.FINANCIAL_ROUTE
    ) {
        composable(route = FinancialDestinations.FINANCIAL_MAIN_ROUTE) {
            FinancialRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { route ->
                    navController.navigate(route)
                },
                onIconMenuClick = onIconMenuClick,
                goHome = {
                    navController.navigate(route = HomeDestinations.HOME_MAIN_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_INFO_ROUTE) {
            FinancialInfoRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_CANCELED_SALES_ROUTE) {
            CanceledSalesRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_STOCK_DEBITS_ROUTE) {
            StockDebitsRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { stockItemId ->
                    navController.navigate(
                        route = "${FinancialDestinations.FINANCIAL_STOCK_ITEM_ROUTE}/$stockItemId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_CUSTOMERS_DEBITS_ROUTE) {
            CustomersDebitRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId, productId ->
                    navController.navigate(
                        route = "${FinancialDestinations.FINANCIAL_SALE_ITEM_ROUTE}/$saleId/$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${FinancialDestinations.FINANCIAL_SALE_ITEM_ROUTE}/{$SALE_ID}/{$PRODUCT_ID}",
            arguments = listOf(
                navArgument(SALE_ID) { type = NavType.LongType },
                navArgument(PRODUCT_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getLong(SALE_ID)
            val productId = backStackEntry.arguments?.getLong(PRODUCT_ID)

            if (saleId != null && productId != null) {
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
        }
        composable(
            route = "${FinancialDestinations.FINANCIAL_STOCK_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { stockItemId ->
                FinancialEditStockItemRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    stockItemId = stockItemId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object FinancialDestinations {
    const val FINANCIAL_MAIN_ROUTE = "financial_main_route"
    const val FINANCIAL_INFO_ROUTE = "financial_info_route"
    const val FINANCIAL_CANCELED_SALES_ROUTE = "financial_canceled_sales_route"
    const val FINANCIAL_CUSTOMERS_DEBITS_ROUTE = "financial_customers_debit_route"
    const val FINANCIAL_STOCK_DEBITS_ROUTE = "financial_stock_debits_route"
    const val FINANCIAL_SALE_ITEM_ROUTE = "financial_sale_item_route"
    const val FINANCIAL_STOCK_ITEM_ROUTE = "financial_stock_item_route"
}