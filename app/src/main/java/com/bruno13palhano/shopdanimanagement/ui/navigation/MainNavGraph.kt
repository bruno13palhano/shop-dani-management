package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.HOME_ROUTE,
    showBottomMenu: (show: Boolean) -> Unit = {},
    gesturesEnabled: (enabled: Boolean) -> Unit = {},
    onIconMenuClick: () -> Unit,
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            homeNavGraph(
                navController = navController,
                sharedTransitionScope = this@SharedTransitionLayout,
                showBottomMenu = showBottomMenu,
                onIconMenuClick = onIconMenuClick,
                gesturesEnabled = gesturesEnabled,
            )
            financialNavGraph(
                sharedTransitionScope = this@SharedTransitionLayout,
                navController = navController,
                showBottomMenu = showBottomMenu,
                onIconMenuClick = onIconMenuClick,
                gesturesEnabled = gesturesEnabled
            )
            insightsNavGraph(
                navController = navController,
                showBottomMenu = showBottomMenu,
                onIconMenuClick = onIconMenuClick,
                gesturesEnabled = gesturesEnabled
            )
            productsNavGraph(
                navController = navController,
                showBottomMenu = showBottomMenu,
                onIconMenuClick = onIconMenuClick,
                gesturesEnabled = gesturesEnabled
            )
            customersNavGraph(
                navController = navController,
                showBottomMenu = showBottomMenu,
                onIconMenuClick = onIconMenuClick,
                gesturesEnabled = gesturesEnabled
            )
            loginNavGraph(
                navController = navController,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled
            )
            userNavGraph(
                navController = navController,
                showBottomMenu = showBottomMenu,
                gesturesEnabled = gesturesEnabled
            )
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home_route"
    const val LOGIN_ROUTE = "login_route"
    const val USER_ROUTE = "user_route"
    const val INSIGHTS_ROUTE = "insights_route"
    const val FINANCIAL_ROUTE = "financial_route"
    const val PRODUCTS_ROUTE = "products_route"
    const val CUSTOMERS_ROUTE = "customers_route"
}
