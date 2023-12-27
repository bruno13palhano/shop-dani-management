package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.CatalogItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.CatalogScreen

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
            showBottomMenu(true)
            gesturesEnabled(true)
            CatalogScreen(
                onItemClick = { id ->
                    navController.navigate(
                        route = "${CatalogDestination.CATALOG_ITEM_ROUTE}/${id}/${true}"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${CatalogDestination.CATALOG_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType },
                navArgument(EDITABLE) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            val id = backStackEntry.arguments?.getLong(ITEM_ID)
            val editable = backStackEntry.arguments?.getBoolean(EDITABLE)
            if (id != null && editable != null) {
                CatalogItemScreen(
                    productId = if (editable) 0L else id,
                    catalogId = if (editable) id else 0L,
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