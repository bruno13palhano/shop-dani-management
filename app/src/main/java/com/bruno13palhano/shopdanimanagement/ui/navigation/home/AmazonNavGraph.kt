package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.AmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.SearchAmazonRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.amazonNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Amazon>(startDestination = AmazonRoutes.Main) {
        composable<AmazonRoutes.Main> {
            AmazonRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId, productId ->
                    navController.navigate(
                        route = AmazonRoutes.Sale(saleId = saleId, productId = productId)
                    )
                },
                onSearchClick = { navController.navigate(route = AmazonRoutes.Search) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<AmazonRoutes.Sale> { backStackEntry ->
            val saleId = backStackEntry.toRoute<AmazonRoutes.Sale>().saleId
            val productId = backStackEntry.toRoute<AmazonRoutes.Sale>().productId

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

        composable<AmazonRoutes.Search> {
            SearchAmazonRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { saleId ->
                    navController.navigate(
                        route = AmazonRoutes.Sale(saleId = saleId, productId = 0L)
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface AmazonRoutes {
    @Serializable
    object Main

    @Serializable
    object Search

    @Serializable
    data class Sale(val saleId: Long, val productId: Long)
}