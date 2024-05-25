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
    data object Home: MainRoutes

    @Serializable
    data object Login: MainRoutes

    @Serializable
    data object Financial: MainRoutes

    @Serializable
    data object Insights: MainRoutes

    @Serializable
    data object Products: MainRoutes

    @Serializable
    data object Customers: MainRoutes

    @Serializable
    data object User: MainRoutes
}
