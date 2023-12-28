package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginScreen
import com.bruno13palhano.shopdanimanagement.ui.screens.user.UserScreen

fun NavGraphBuilder.userNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation(
        startDestination = UserDestinations.USER_MAIN_ROUTE,
        route = MainDestinations.USER_ROUTE
    ) {
        composable(route = UserDestinations.USER_MAIN_ROUTE) {
            showBottomMenu(false)
            gesturesEnabled(true)
            UserScreen(
                onLogout = {
                    navController.navigate(route = UserDestinations.USER_LOGIN_ROUTE) {
                        popUpTo(0)
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = UserDestinations.USER_LOGIN_ROUTE) {
            showBottomMenu(false)
            gesturesEnabled(false)
            LoginScreen(
                onSuccess = {
                    navController.navigate(route = HomeDestinations.HOME_MAIN_ROUTE) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

object UserDestinations {
    const val USER_MAIN_ROUTE = "user_main_route"
    const val USER_LOGIN_ROUTE = "user_login_route"
}