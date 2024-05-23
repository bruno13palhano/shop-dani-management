package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.PRODUCT_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.SALE_ID
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.AmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.SearchAmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.amazonNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
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
            route = "${AmazonDestinations.AMAZON_SALE_ROUTE}/{$SALE_ID}/{$PRODUCT_ID}",
            arguments = listOf(
                navArgument(SALE_ID) { type = NavType.LongType },
                navArgument(PRODUCT_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getLong(SALE_ID)
            val productId = backStackEntry.arguments?.getLong(PRODUCT_ID)

            if (saleId != null && productId != null) {
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