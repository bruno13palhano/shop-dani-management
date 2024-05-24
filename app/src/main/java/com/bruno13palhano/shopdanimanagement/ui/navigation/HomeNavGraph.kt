package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.amazonNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.catalogNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.deliveriesNAvGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.ordersNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.salesNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.stockNavGraph
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.EditSaleRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation<MainRoutes.Home>(startDestination = HomeRoutes.Main) {
        composable<HomeRoutes.Main> {
            HomeRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onOptionsItemClick = { route ->
                    navController.navigate(route = route)
                },
                onSalesItemClick = { id ->
                    navController.navigate(route = HomeRoutes.Sale(id = id))
                },
                onMenuClick = onIconMenuClick,
                onUnauthenticated = {
                    navController.navigate(route = LoginRoutes.Main) {
                        popUpTo(route = HomeRoutes.Main) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<HomeRoutes.Sale> { backStackEntry ->
            backStackEntry.toRoute<HomeRoutes.Sale>().id.let { saleId ->
                EditSaleRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    saleId = saleId,
                    productId = 0L,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = this@composable,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }

        salesNavGraph(
            navController = navController,
            sharedTransitionScope = sharedTransitionScope,
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
            sharedTransitionScope = sharedTransitionScope,
            showBottomMenu = showBottomMenu,
            gesturesEnabled = gesturesEnabled
        )

        amazonNavGraph(
            navController = navController,
            sharedTransitionScope = sharedTransitionScope,
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

sealed interface HomeRoutes {
    @Serializable
    object Main

    @Serializable
    object Stock

    @Serializable
    object Sales

    @Serializable
    object Orders

    @Serializable
    object Amazon

    @Serializable
    object Deliveries

    @Serializable
    object Catalog

    @Serializable
    data class Sale(val id: Long)
}