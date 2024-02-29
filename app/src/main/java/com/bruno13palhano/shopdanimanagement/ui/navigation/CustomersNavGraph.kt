package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.EditCustomerRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.NewCustomerRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.SearchCustomersRoute

fun NavGraphBuilder.customersNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = CustomersDestinations.MAIN_CUSTOMERS_ROUTE,
        route = MainDestinations.CUSTOMERS_ROUTE
    ) {
        composable(route = CustomersDestinations.MAIN_CUSTOMERS_ROUTE) {
            CustomersRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}/$id"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE
                    )
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = CustomersDestinations.CUSTOMERS_NEW_CUSTOMER_ROUTE
                    )
                },
                onIconMenuClick = onIconMenuClick
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE) {
            SearchCustomersRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onItemClick = { id ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}/$id"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_NEW_CUSTOMER_ROUTE) {
            NewCustomerRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}/{$ITEM_ID}",
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { customerId ->
                EditCustomerRoute(
                    showBottomMenu = showBottomMenu,
                    gesturesEnabled = gesturesEnabled,
                    customerId = customerId,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object CustomersDestinations {
    const val MAIN_CUSTOMERS_ROUTE = "customers_main_route"
    const val CUSTOMERS_SEARCH_ROUTE = "customers_search_route"
    const val CUSTOMERS_NEW_CUSTOMER_ROUTE = "customers_new_customer_route"
    const val CUSTOMERS_EDIT_CUSTOMER_ROUTE = "customers_edit_customer_route"
}