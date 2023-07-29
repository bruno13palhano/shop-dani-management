package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.navigation.NavController

class BottomMenuNavActions(navController: NavController) {

    val navigateFromBottomMenu: (route: String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(MainDestinations.HOME_ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}