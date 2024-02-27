package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.AmazonScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.SearchAmazonScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SaleScreen

fun NavGraphBuilder.amazonNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = AmazonDestinations.MAIN_AMAZON_ROUTE,
        route = HomeDestinations.HOME_AMAZON_ROUTE
    ) {
        composable(route = AmazonDestinations.MAIN_AMAZON_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            AmazonScreen(
                onItemClick = { saleId ->
                    navController.navigate(
                        route = "${AmazonDestinations.AMAZON_SALE_ROUTE}/$saleId"
                    )
                },
                onSearchClick = {
                    navController.navigate(route = AmazonDestinations.AMAZON_SEARCH_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${AmazonDestinations.AMAZON_SALE_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)

            id?.let { saleId ->
                SaleScreen(
                    isEdit = true,
                    screenTitle = stringResource(id = R.string.edit_sale_label),
                    isOrderedByCustomer = true,
                    stockOrderId = 0L,
                    saleId = saleId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = AmazonDestinations.AMAZON_SEARCH_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            SearchAmazonScreen(
                onItemClick = { id ->
                    navController.navigate(route = "${AmazonDestinations.AMAZON_SALE_ROUTE}/$id")
                },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object AmazonDestinations {
    const val MAIN_AMAZON_ROUTE = "main_amazon_route"
    const val AMAZON_SEARCH_ROUTE = "amazon_search_route"
    const val AMAZON_SALE_ROUTE = "amazon_sale_route"
}