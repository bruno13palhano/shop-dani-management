package com.bruno13palhano.shopdanimanagement.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showBottomMenu: (show: Boolean) -> Unit = {},
    gesturesEnabled: (enabled: Boolean) -> Unit = {},
    onIconMenuClick: () -> Unit,
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Home,
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

sealed interface MainRoutes {

    @Serializable
    object Home

    @Serializable
    object Login

    @Serializable
    object Financial

    @Serializable
    object Insights

    @Serializable
    object Products

    @Serializable
    object Customers

    @Serializable
    object User
}
