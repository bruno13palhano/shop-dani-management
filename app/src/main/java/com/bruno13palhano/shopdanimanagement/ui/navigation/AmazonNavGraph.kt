package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.AmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.SearchAmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute

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
            AmazonRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
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
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { saleId ->
                EditSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    saleId = saleId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = AmazonDestinations.AMAZON_SEARCH_ROUTE) {
            SearchAmazonRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
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