package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.shopdanimanagement.ui.screens.user.ChangePasswordRoute
import com.bruno13palhano.shopdanimanagement.ui.screens.user.UserRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.userNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit
) {
    navigation<MainRoutes.User>(startDestination = UserRoutes.Main) {
        composable<UserRoutes.Main> {
            UserRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                onLogoutClick = {
                    navController.navigate(route = MainRoutes.Login) {
                        popUpTo(0)
                    }
                },
                onChangePasswordClick = {
                    navController.navigate(route = UserRoutes.ChangePassword)
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<UserRoutes.ChangePassword> {
            ChangePasswordRoute(
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed interface UserRoutes {
    @Serializable
    object Main

    @Serializable
    object ChangePassword
}