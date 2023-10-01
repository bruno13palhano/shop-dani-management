package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CustomersDebitScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialInfoScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.FinancialScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.StockDebitsScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.ItemScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.financialNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = FinancialDestinations.FINANCIAL_MAIN_ROUTE,
        route = MainDestinations.FINANCIAL_ROUTE
    ) {
        composable(route = FinancialDestinations.FINANCIAL_MAIN_ROUTE) {
            showBottomMenu(false)
            FinancialScreen(
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
            showBottomMenu(false)
            FinancialInfoScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_STOCK_DEBITS_ROUTE) {
            showBottomMenu(false)
            StockDebitsScreen(
                onItemClick = { stockItemId ->
                    navController.navigate(
                        route = "${FinancialDestinations.FINANCIAL_STOCK_ITEM_ROUTE}$stockItemId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_CUSTOMERS_DEBITS_ROUTE) {
            showBottomMenu(false)
            CustomersDebitScreen(
                onItemClick = { saleId ->
                    navController.navigate(
                        route = "${FinancialDestinations.FINANCIAL_SALE_ITEM_ROUTE}$saleId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = FinancialDestinations.FINANCIAL_SALE_ITEM_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { saleId ->
                SaleScreen(
                    isEdit = true,
                    screenTitle = stringResource(id = R.string.edit_sale_label),
                    isOrderedByCustomer = false,
                    stockOrderId = 0L,
                    saleId = saleId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = FinancialDestinations.FINANCIAL_STOCK_ITEM_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { stockItemId ->
                ItemScreen(
                    isEditable = true,
                    isOrderedByCustomer = false,
                    productId = 0L,
                    stockOrderItemId = stockItemId.toLong(),
                    screenTitle = stringResource(id = R.string.edit_stock_item_label),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object FinancialDestinations {
    const val FINANCIAL_MAIN_ROUTE = "financial_main_route"
    const val FINANCIAL_INFO_ROUTE = "financial_info_route"
    const val FINANCIAL_CUSTOMERS_DEBITS_ROUTE = "financial_customers_debit_route"
    const val FINANCIAL_STOCK_DEBITS_ROUTE = "financial_stock_debits_route"
    const val FINANCIAL_SALE_ITEM_ROUTE = "financial_sale_item_route"
    const val FINANCIAL_SALE_ITEM_WITH_ID_ROUTE = "$FINANCIAL_SALE_ITEM_ROUTE{$ITEM_ID}"
    const val FINANCIAL_STOCK_ITEM_ROUTE = "financial_stock_item_route"
    const val FINANCIAL_STOCK_ITEM_WITH_ID_ROUTE = "$FINANCIAL_STOCK_ITEM_ROUTE{$ITEM_ID}"
}