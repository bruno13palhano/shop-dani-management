package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerInfoScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.SearchCustomersScreen

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
            showBottomMenu(true)
            gesturesEnabled(true)
            CustomersScreen(
                onItemClick = { customerId ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_ROUTE}/$customerId"
                    )
                },
                onSearchClick = {
                    navController.navigate(
                        route = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE
                    )
                },
                onAddButtonClick = {
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/${0L}/${false}"
                    )
                },
                onIconMenuClick = onIconMenuClick
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE) {
            showBottomMenu(true)
            gesturesEnabled(true)
            SearchCustomersScreen(
                onItemClick = { id ->
                    navController.navigate(
                        route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/$id/${true}"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/{$ITEM_ID}/{$EDITABLE}",
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
                CustomerScreen(
                    screenTitle = if (editable) {
                        stringResource(id = R.string.edit_customer_label)
                    }
                    else {
                        stringResource(id = R.string.new_customer_label)
                    },
                    isEditable = editable,
                    customerId = if (editable) id else 0L,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_ROUTE}/{$ITEM_ID}",
            arguments = listOf(
                navArgument(ITEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            showBottomMenu(true)
            gesturesEnabled(true)
            backStackEntry.arguments?.getLong(ITEM_ID)?.let { id ->
                CustomerInfoScreen(
                    customerId = id,
                    onEditIconClick = {
                        navController.navigate(
                            route = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/$id/${true}"
                        )
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object CustomersDestinations {
    const val MAIN_CUSTOMERS_ROUTE = "customers_main_route"
    const val CUSTOMERS_SEARCH_ROUTE = "customers_search_route"
    const val CUSTOMERS_CUSTOMER_ROUTE = "customers_customer_route"
    const val CUSTOMERS_CUSTOMER_INFO_ROUTE = "customers_customer_info_route"
}