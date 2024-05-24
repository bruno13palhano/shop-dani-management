package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.login.CreateAccountRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<MainRoutes.Login>(startDestination = LoginRoutes.Main) {
        composable<LoginRoutes.Main> {
            LoginRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onSuccess = {
                    navController.navigate(route = HomeRoutes.Main) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(route = LoginRoutes.CreateAccount)
                }
            )
        }

        composable<LoginRoutes.CreateAccount> {
            CreateAccountRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onSuccess = {
                    navController.navigate(route = HomeRoutes.Main) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface LoginRoutes {
    @Serializable
    object Main

    @Serializable
    object CreateAccount
}