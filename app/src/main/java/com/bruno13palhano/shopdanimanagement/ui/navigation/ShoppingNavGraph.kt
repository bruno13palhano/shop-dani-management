package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.ShoppingScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.EditShoppingItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.NewItemScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ProductItemListScreen

private const val ITEM_ID = "item_Id"

fun NavGraphBuilder.shoppingNavGraph(
    navController: NavController,
    onMenuClick: () -> Unit,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation(
        startDestination = ShoppingDestinations.MAIN_SHOPPING_ROUTE,
        route = MainDestinations.SHOPPING_ROUTE,
    ) {
        composable(route = ShoppingDestinations.MAIN_SHOPPING_ROUTE) {
            showBottomMenu(true)
            ShoppingScreen(
                onItemClick = { shoppingItemId ->
                    navController.navigate(
                        route = "${ShoppingDestinations.SHOPPING_EDIT_SHOPPING_PRODUCT_ROUTE}$shoppingItemId"
                    )
                },
                onMenuClick = onMenuClick,
                onAddButtonClick = {
                    navController.navigate(route = ShoppingDestinations.SHOPPING_PRODUCT_LIST_ROUTE)
                }
            )
        }
        composable(route = ShoppingDestinations.SHOPPING_PRODUCT_LIST_ROUTE) {
            showBottomMenu(true)
            ProductItemListScreen(
                onItemClick = { productId ->
                    navController.navigate(
                        route = "${ShoppingDestinations.SHOPPING_NEW_SHOPPING_PRODUCT_ROUTE}$productId"
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ShoppingDestinations.SHOPPING_NEW_SHOPPING_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { productId ->
                NewItemScreen(
                    isOrderedByCustomer = false,
                    screenTitle = stringResource(id = R.string.new_shopping_item_label),
                    productId = productId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
        composable(route = ShoppingDestinations.SHOPPING_EDIT_SHOPPING_PRODUCT_WITH_ID_ROUTE) { backStackEntry ->
            showBottomMenu(true)
            backStackEntry.arguments?.getString(ITEM_ID)?.let { shoppingItemId ->
                EditShoppingItemScreen(
                    shoppingItemId = shoppingItemId.toLong(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}

object ShoppingDestinations {
    const val MAIN_SHOPPING_ROUTE = "main_shopping_route"
    const val SHOPPING_PRODUCT_LIST_ROUTE = "shopping_product_list_route"
    const val SHOPPING_NEW_SHOPPING_PRODUCT_ROUTE = "shopping_new_shopping_product_route"
    const val SHOPPING_NEW_SHOPPING_PRODUCT_WITH_ID_ROUTE = "$SHOPPING_NEW_SHOPPING_PRODUCT_ROUTE{$ITEM_ID}"
    const val SHOPPING_EDIT_SHOPPING_PRODUCT_ROUTE = "shopping_edit_shopping_product_route"
    const val SHOPPING_EDIT_SHOPPING_PRODUCT_WITH_ID_ROUTE = "$SHOPPING_EDIT_SHOPPING_PRODUCT_ROUTE{$ITEM_ID}"
}