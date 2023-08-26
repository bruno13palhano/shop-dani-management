package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.EditCustomerScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.NewCustomerScreen

private const val ITEM_ID = "item_id"

fun NavGraphBuilder.customersNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = CustomersDestinations.MAIN_CUSTOMERS_ROUTE,
        route = MainDestinations.CUSTOMERS_ROUTE
    ) {
        composable(route = CustomersDestinations.MAIN_CUSTOMERS_ROUTE) {
            showBottomMenu(false)
            CustomersScreen(
                onItemClick = { customerId ->
                    navController.navigate(route = "${CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_ROUTE}$customerId")
                },
                onAddButtonClick = {
                    navController.navigate(route = CustomersDestinations.CUSTOMERS_NEW_CUSTOMER_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_NEW_CUSTOMER_ROUTE) {
            showBottomMenu(false)
            NewCustomerScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = CustomersDestinations.CUSTOMERS_EDIT_CUSTOMER_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(false)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { customerId ->
                EditCustomerScreen(
                    customerId = customerId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object CustomersDestinations {
    const val MAIN_CUSTOMERS_ROUTE = "customers_main_route"
    const val CUSTOMERS_NEW_CUSTOMER_ROUTE = "customers_new_customer_route"
    const val CUSTOMERS_EDIT_CUSTOMER_ROUTE = "customers_edit_customers_route"
    const val CUSTOMERS_EDIT_CUSTOMER_WITH_ID_ROUTE = "$CUSTOMERS_EDIT_CUSTOMER_ROUTE{$ITEM_ID}"
}