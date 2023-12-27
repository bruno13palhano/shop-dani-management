package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginScreen

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
            showBottomMenu(false)
            gesturesEnabled(false)
            LoginScreen(
                onSuccess = {
                    navController.navigate(route = HomeDestinations.HOME_MAIN_ROUTE) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = LoginDestinations.LOGIN_CREATE_ACCOUNT_ROUTE) {
            showBottomMenu(false)
            gesturesEnabled(false)
        }
    }
}

object LoginDestinations {
    const val LOGIN_MAIN_ROUTE = "login_main_route"
    const val LOGIN_CREATE_ACCOUNT_ROUTE = "login_create_account_route"
}