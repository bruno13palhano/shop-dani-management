package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = HomeDestinations.HOME_MAIN_ROUTE,
        route = MainDestinations.HOME_ROUTE,
    ) {
        composable(route = HomeDestinations.HOME_MAIN_ROUTE) {
            gesturesEnabled(true)
            HomeScreen(
                onOptionsItemClick = { route ->
                    navController.navigate(route = route)
                },
                onSalesItemClick = { id, isOrderedByCustomer ->
                    navController.navigate(
                        route = "${HomeDestinations.HOME_SALE_ROUTE}/$id/$isOrderedByCustomer"
                    )
                },
                onMenuClick = onIconMenuClick,
                onUnauthenticated = {
                    navController.navigate(route = LoginDestinations.LOGIN_MAIN_ROUTE) {
                        popUpTo(route = HomeDestinations.HOME_MAIN_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                showBottomMenu = showBottomMenu
            )
        }

        composable(
            route = "${HomeDestinations.HOME_SALE_ROUTE}/{$ITEM_ID}/{$IS_ORDERED}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(IS_ORDERED) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            val saleId = backStackEntry.arguments?.getLong(ITEM_ID)
            val isOrdered = backStackEntry.arguments?.getBoolean(IS_ORDERED)

            if (saleId != null && isOrdered != null) {
                SaleScreen(
                    isEdit = true,
                    screenTitle = stringResource(id = R.string.edit_sale_label),
                    isOrderedByCustomer = isOrdered,
                    stockOrderId = 0L,
                    saleId = saleId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }

        salesNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
        stockNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
        ordersNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
        amazonNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
        deliveriesNAvGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
        catalogNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )
    }
}

object HomeDestinations {
    const val HOME_MAIN_ROUTE = "home_main_route"
    const val HOME_STOCK_ROUTE = "home_stock_route"
    const val HOME_SALES_ROUTE = "home_sales_route"
    const val HOME_ORDERS_ROUTE = "home_orders_route"
    const val HOME_AMAZON_ROUTE = "home_amazon_route"
    const val HOME_DELIVERIES_ROUTE = "home_deliveries_route"
    const val HOME_CATALOG_ROUTE = "home_catalog_route"
    const val HOME_SALE_ROUTE = "home_sale_route"
}