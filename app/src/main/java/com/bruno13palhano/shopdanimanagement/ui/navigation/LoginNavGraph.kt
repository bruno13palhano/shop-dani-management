package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.login.CreateAccountRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginRoute

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = LoginDestinations.LOGIN_MAIN_ROUTE,
        route = MainDestinations.LOGIN_ROUTE
    ) {
        composable(route = LoginDestinations.LOGIN_MAIN_ROUTE) {
            LoginRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onSuccess = {
                    navController.navigate(route = MainDestinations.HOME_ROUTE) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(route = LoginDestinations.LOGIN_CREATE_ACCOUNT_ROUTE)
                }
            )
        }
        composable(route = LoginDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
            CreateAccountRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onSuccess = {
                    navController.navigate(route = HomeDestinations.HOME_MAIN_ROUTE) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

object LoginDestinations {
    const val LOGIN_MAIN_ROUTE = "login_main_route"
    const val LOGIN_CREATE_ACCOUNT_ROUTE = "login_create_account_route"
}