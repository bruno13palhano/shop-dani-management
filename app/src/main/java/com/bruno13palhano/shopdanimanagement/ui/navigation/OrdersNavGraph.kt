package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

fun NavGraphBuilder.ordersBavGraph(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    navigation(
        startDestination = OrdersDestinations.MAIN_ORDERS_ROUTE,
        route = MainDestinations.ORDERS_ROUTE
    ) {

    }
}

object OrdersDestinations {
    const val MAIN_ORDERS_ROUTE = "main_orders_route"
    const val ORDERS_CATEGORIES_ROUTE = "orders_categories_route"
}