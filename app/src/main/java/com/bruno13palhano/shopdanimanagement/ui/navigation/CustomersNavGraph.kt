package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerInfoScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.SearchCustomersScreen

private const val ITEM_ID = "item_id"

fun NavGraphBuilder.customersNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    onIconMenuClick: () -> Unit
) {
    navigation(
        startDestination = CustomersDestinations.MAIN_CUSTOMERS_ROUTE,
        route = MainDestinations.CUSTOMERS_ROUTE
    ) {
        composable(route = CustomersDestinations.MAIN_CUSTOMERS_ROUTE) {
            showBottomMenu(true)
            CustomersScreen(
                onItemClick = { customerId ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_ROUTE}$customerId"
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
            showBottomMenu(true)
            SearchCustomersScreen(
                onItemClick = { customerId ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}$customerId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_NEW_CUSTOMER_ROUTE) {
            showBottomMenu(true)
            CustomerScreen(
                screenTitle = stringResource(id = R.string.new_customer_label),
                isEditable = false,
                customerId = 0L,
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { customerId ->
                CustomerInfoScreen(
                    customerId = customerId.toLong(),
                    onEditIconClick = {
                        navController.navigate(
                            route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}$customerId"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { customerId ->
                CustomerScreen(
                    screenTitle = stringResource(id = R.string.edit_customer_label),
                    isEditable = true,
                    customerId = customerId.toLong(),
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
    const val CUSTOMERS_CUSTOMER_INFO_ROUTE = "customers_customer_info_route"
    const val CUSTOMERS_CUSTOMER_INFO_WITH_ID_ROUTE = "$CUSTOMERS_CUSTOMER_INFO_ROUTE{$ITEM_ID}"
    const val CUSTOMERS_EDIT_CUSTOMER_ROUTE = "customers_edit_customers_route"
    const val CUSTOMERS_EDIT_CUSTOMER_WITH_ID_ROUTE = "$CUSTOMERS_EDIT_CUSTOMER_ROUTE{$ITEM_ID}"
}