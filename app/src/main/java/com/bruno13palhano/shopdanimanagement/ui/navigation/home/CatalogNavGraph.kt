package com.bruno13palhano.shopdanimanagement.ui.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.CatalogRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.EditCatalogItemRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.catalogNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<HomeRoutes.Catalog>(startDestination = CatalogRoutes.Main) {
        composable<CatalogRoutes.Main> {
            CatalogRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(route = CatalogRoutes.Item(id))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<CatalogRoutes.Item> { backStackEntry ->
            val catalogId = backStackEntry.toRoute<CatalogRoutes.Item>().id

            EditCatalogItemRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                catalogId = catalogId,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface CatalogRoutes {
    @Serializable
    object Main

    @Serializable
    data class Item(val id: Long)
}