package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.EditCustomerRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.NewCustomerRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.SearchCustomersRoute
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.customersNavGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation<MainRoutes.Customers>(startDestination = CustomersRoutes.Main) {
        composable<CustomersRoutes.Main> {
            CustomersRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(route = CustomersRoutes.EditCustomer(id = id))
                },
                onSearchClick = {
                    navController.navigate(route = CustomersRoutes.Search)
                },
                onAddButtonClick = {
                    navController.navigate(route = CustomersRoutes.NewCustomer)
                },
                onIconMenuClick = onIconMenuClick
            )
        }
        composable<CustomersRoutes.Search> {
            SearchCustomersRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(route = CustomersRoutes.EditCustomer(id = id))
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<CustomersRoutes.NewCustomer> {
            NewCustomerRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<CustomersRoutes.EditCustomer> { backStackEntry ->
            val customerId = backStackEntry.toRoute<CustomersRoutes.EditCustomer>().id

            EditCustomerRoute(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                customerId = customerId,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface CustomersRoutes {
    @Serializable
    object Main

    @Serializable
    object Search

    @Serializable
    object NewCustomer

    @Serializable
    data class EditCustomer(val id: Long)
}