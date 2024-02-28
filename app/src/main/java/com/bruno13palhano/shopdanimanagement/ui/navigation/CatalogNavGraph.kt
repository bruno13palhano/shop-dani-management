package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.CatalogRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.EditCatalogItemRoute

fun NavGraphBuilder.catalogNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = CatalogDestination.CATALOG_MAIN_ROUTE,
        route = HomeDestinations.HOME_CATALOG_ROUTE
    ) {
        composable(route = CatalogDestination.CATALOG_MAIN_ROUTE) {
            CatalogRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(
                        route = "${CatalogDestination.CATALOG_ITEM_ROUTE}/$id"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${CatalogDestination.CATALOG_ITEM_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { catalogId ->
                EditCatalogItemRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    catalogId = catalogId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object CatalogDestination {
    const val CATALOG_MAIN_ROUTE = "catalog_main_route"
    const val CATALOG_ITEM_ROUTE = "catalog_item_route"
}