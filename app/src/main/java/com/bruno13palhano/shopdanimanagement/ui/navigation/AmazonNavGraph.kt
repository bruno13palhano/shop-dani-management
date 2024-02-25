package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.amazonNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = AmazonDestinations.MAIN_AMAZON_ROUTE,
        route = HomeDestinations.HOME_AMAZON_ROUTE
    ) {
        composable(route = AmazonDestinations.MAIN_AMAZON_ROUTE) {

        }
    }
}

object AmazonDestinations {
    const val MAIN_AMAZON_ROUTE = "main_amazon_route"
}