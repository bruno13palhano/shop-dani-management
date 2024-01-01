package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.user.ChangePasswordScreen
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
                onLogoutClick = {
                    navController.navigate(route = MainDestinations.LOGIN_ROUTE) {
                        popUpTo(0)
                    }
                },
                onChangePasswordClick = {
                    navController.navigate(route = UserDestinations.USER_CHANGE_PASSWORD_ROUTE)
                },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(route = UserDestinations.USER_CHANGE_PASSWORD_ROUTE) {
            showBottomMenu(false)
            gesturesEnabled(true)
            ChangePasswordScreen(navigateUp = { navController.navigateUp() })
        }
    }
}

object UserDestinations {
    const val USER_MAIN_ROUTE = "user_main_route"
    const val USER_CHANGE_PASSWORD_ROUTE = "user_change_password_route"
}